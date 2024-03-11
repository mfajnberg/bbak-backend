package de.mfberg.bbak.jobs;

import lombok.Builder;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Builder
public class TravelJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(TravelJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobInfo jobInfo = (JobInfo) jobDataMap.get(TravelJob.class.getSimpleName());
        LOG.info("~~~> traveling......." + jobInfo.getCallbackData());
    }
}
