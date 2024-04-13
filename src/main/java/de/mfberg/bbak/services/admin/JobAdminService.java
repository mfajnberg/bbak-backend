package de.mfberg.bbak.services.admin;

import de.mfberg.bbak.services.common.QuartzService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobAdminService {
    private final QuartzService quartzService;
    public Map<String, String> getScheduledJobs() throws SchedulerException {
        Map<String, String> scheduledJobs = new HashMap<>();
        Scheduler scheduler = quartzService.getScheduler();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    if (trigger.getNextFireTime() != null) {
                        scheduledJobs.put(jobKey.toString(), trigger.getNextFireTime().toString());
                        // todo: add more info about the job (do account for different job groups)
                    }
                }
            }
        }
        return scheduledJobs;
    }
}
