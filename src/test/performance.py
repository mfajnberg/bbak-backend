import sys
import subprocess
import ssl
import aiohttp
from aiohttp import TCPConnector
import asyncio
import random
import string
import json
import psutil
import pandas as pd
import matplotlib.pyplot as plt
import statsmodels.api as sm
import time
import datetime
from typing import Tuple, List, Dict, Optional

def get_app_pid() -> int:
    """
    Retrieve the process ID for the specified application type.

    Returns:
        int: The process ID of the running application.
    Raises:
        ValueError: If no process ID is found.
        SystemExit: On failure to find process, terminates the script.
    """
    try:
        process_name = "bbak-0.0.1-SNAPSHOT.jar" if APP_TYPE == "spring" else "web-api.dll"
        result = subprocess.run(["pgrep", "-f", process_name], capture_output=True, text=True)
        pid = int(result.stdout.strip())
        if pid:
            return pid
        raise ValueError("Process ID not found")
    except Exception as e:
        print(f"Error finding PID: {str(e)}")
        sys.exit(1)

def generate_user_data(count: int) -> List[Dict[str, str]]:
    """
    Generate user data with random emails and passwords.

    Args:
        count (int): The number of users to generate data for.

    Returns:
        list: A list of dictionaries with email and password keys.
    """
    return [{
        "email": ''.join(random.choices(string.ascii_letters + string.digits, k=10)) + "@example.com",
        "password": ''.join(random.choices(string.ascii_letters + string.digits, k=12))
    } for _ in range(count)]

async def register_user(
        session: aiohttp.ClientSession, 
        url: str, 
        data: Dict[str, str]
    ) -> Optional[str]:
    """
    Register a user via POST request and retrieve a token.

    Args:
        session (aiohttp.ClientSession): The session object.
        url (str): The URL to send the POST request to.
        data (dict): The data payload for the POST request.

    Returns:
        str: The token received in the response.
    """
    async with session.post(url, json=data) as response:
        response_data = await response.json()
        return response_data.get('access_token')

def measure_resource_usage() -> Tuple[float, int]:
    """
    Measure CPU and memory usage for the application.

    Returns:
        tuple: A tuple containing CPU percent and memory usage.
    """
    process = psutil.Process(APP_PID)
    return process.cpu_percent(interval=1), process.memory_info().rss

async def test_api_performance(
        session: aiohttp.ClientSession, 
        api_url: str, 
        jwt_token: str, 
        headers: Dict[str, str], 
        method: str, 
        data: Dict[str, str]
    ) -> Dict[str, any]:
    """
    Test API performance by making one request and measuring the response statistics.
    If the endpoint provides a token (e.g. POST "auth/login"), 
    then it's included in the returned dictionary - else the old one is.

    Args:
        session (aiohttp.ClientSession): The session to use for the HTTP request.
        api_url (str): The URL to request.
        jwt_token (str): JWT token for authorization.
        headers (dict): Request headers.
        method (str): HTTP method to use.
        data (dict): Data to send in the request.

    Returns:
        dict: A dictionary of performance data for a single request (and potentially a new token).
    """
    start_time = time.monotonic()
    async with session.request(method, api_url, headers=headers, json=data) as response:
        response_data = await response.json()
        print(response.status) 
    end_time = time.monotonic()
    return {
        "Endpoint": api_url,
        "Response Time": end_time - start_time,
        "Timestamp": time.time(),
        "Token": response_data.get('token', jwt_token)
    }

async def run_user_story(
        session: aiohttp.ClientSession, 
        endpoints: List[Dict[str, any]], 
        initial_token: str
    ) -> List[Dict[str, any]]:
    """
    Run a sequence of API requests representing a single user's user story.

    Args:
        session (aiohttp.ClientSession): The session to use for the HTTP requests.
        endpoints (list): A list of endpoint information (URL, method, data).
        initial_token (str): The initial JWT token for the user. 
            Might get mutated depending on the exact sequence of requested endpoints.

    Returns:
        list: A list of performance statistics of one user's sequentially requested endpoints.
    """
    current_token = initial_token
    results = []
    for endpoint in endpoints:
        result = await test_api_performance(session, endpoint["url"], current_token, {
            'Authorization': f'Bearer {current_token}',
            'Content-Type': 'application/json'
        }, endpoint["method"], endpoint["data"])
        current_token = result['Token']
        results.append(result)
    return results

async def simulate_load_increase(
        session: aiohttp.ClientSession, 
        user_data: List[Dict[str, str]], 
        tokens: List[str]
    ) -> List[List[Dict[str, any]]]:
    """
    Simulate an increase in load by running user stories in parallel for an increasing number of users.

    Args:
        session (aiohttp.ClientSession): The session to use for API requests.
        user_data (list): List of user data.
        tokens (list): List of tokens for each user.

    Returns:
        list: a list of load levels, where each load level contains a list of user data,
            and each user data contains a list of endpoint performance data dictionaries.
    """
    results = []
    for i in range(1, len(user_data) + 1):
        user_stories = [[{
                "url": f"{BASE_URL}/auth/login",
                "method": "POST",
                "data": {"email": user["email"], "password": user["password"]}
            },
        ] for user in user_data[:i]]
        tasks = [
            run_user_story(session, story, token) 
            for story, token in zip(user_stories, tokens)
        ]
        results.append(await asyncio.gather(*tasks))
        print(f"{i} users authenticated.")
    return results

async def main() -> None:
    """
    Main execution function to run the load test simulation and then analyze and save the results.
    """
    # Disable SSL verification
    ssl_context = ssl.create_default_context()
    ssl_context.check_hostname = False
    ssl_context.verify_mode = ssl.CERT_NONE
    
    connector = TCPConnector(ssl=ssl_context)
    async with aiohttp.ClientSession(connector=connector) as session:
        user_data = generate_user_data(MAX_USER_COUNT)
        tokens = await asyncio.gather(*(register_user(session, f"{BASE_URL}/auth/register", user) for user in user_data))
        results = await simulate_load_increase(session, user_data, tokens)
    analyze_and_plot_results(results)


def analyze_and_plot_results(data):
    """
    Analyze and visualize average response times across different load levels.
    Fit a linear regression model to estimate trends in response times as load increases.

    Args:
        data (List[List[List[Dict[str, any]]]]): The test data structured by load levels,
            containing response times for each endpoint call.
    """
    load_levels = []
    avg_response_times_ms = []  # Average response times in milliseconds.

    # Aggregate, convert to ms.
    for load_index, load_level in enumerate(data):
        load_levels.append(load_index + 1)
        all_endpoints = [endpoint for user in load_level for endpoint in user]
        total_response_time = sum(endpoint['Response Time'] for endpoint in all_endpoints)
        num_endpoints = len(all_endpoints)
        avg_response_times_ms.append((total_response_time / num_endpoints) * 1000)

    df = pd.DataFrame({
        'Load Level': load_levels,
        'Average Response Time (ms)': avg_response_times_ms
    })

    # Linear regression
    X = sm.add_constant(df['Load Level'])  # Add a constant for the intercept.
    model = sm.OLS(df['Average Response Time (ms)'], X).fit()

    # Plot data & the regression line.
    fig, ax = plt.subplots(figsize=(10, 7))
    ax.plot(df['Load Level'], df['Average Response Time (ms)'], marker='o', label='Data')
    ax.plot(df['Load Level'], model.predict(), linestyle=':', color='green', label='Regression Line')
    ax.set_title(f'Average Response Time vs. Load Level [{APP_TYPE.upper()}]')
    ax.set_xlabel('Load Level')
    ax.set_ylabel('Average Response Time (ms)')

    ax.set_xticks(df['Load Level'][4::5])
    ax.set_xticklabels(df['Load Level'][4::5], rotation=45)

    ax.grid(True, which='major', axis='y', linestyle='--', alpha=0.7)
    ax.legend()
    plt.tight_layout()
    plt.savefig(f'load_test_plots_{APP_TYPE.upper()}_{MAX_USER_COUNT}_{TIMESTAMP}.pdf')
    plt.show()

    results_data = {
        "Slope": model.params[1],  # How much the response time increases per unit increase in load level.
        "Intercept": model.params[0],  # The estimated response time at zero load (theoretical).
        "R-squared": model.rsquared,  # Proportion of variance in response times explained by the model.
        "p-values": model.pvalues[1],  # Significance of the slope coefficient.
        "Confidence Intervals": model.conf_int().iloc[1].tolist()  # 95% confidence interval for the slope.
    }
    with open(f'regression_results_{APP_TYPE.upper()}_{MAX_USER_COUNT}_{TIMESTAMP}.json', 'w') as f:
        json.dump(results_data, f, indent=4)

if __name__ == "__main__":
    global TIMESTAMP, APP_TYPE, MAX_USER_COUNT, APP_PID, BASE_URL
    TIMESTAMP = datetime.datetime.now().strftime("%Y-%m-%d-%H-%M")
    if len(sys.argv) != 3:
        print("Usage: python load_test.py <application_type> <max_user_count>")
        sys.exit(1)
    APP_TYPE = sys.argv[1].lower()
    try:
        MAX_USER_COUNT = int(sys.argv[2])
    except ValueError:
        print("Invalid number for max user count")
        sys.exit(1)
    BASE_URL = 'http://localhost:8080/api' if APP_TYPE == "spring" else 'http://localhost:5000/api'
    asyncio.run(main())