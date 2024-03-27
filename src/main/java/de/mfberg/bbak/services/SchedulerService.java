package de.mfberg.bbak.services;

import de.mfberg.bbak.jobs.TravelData;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class SchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler quarz;

    public Scheduler getScheduler() {
        return quarz;
    }

    @PostConstruct
    public void init() {
        try {
            quarz.start();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            quarz.shutdown();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public JobDetail buildJobDetail(final Class jobClass, final Object jobData) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobData", jobData);

        return JobBuilder
                .newJob(jobClass)
                // todo: identity should associate the party and 'handle' collisions.
                .withIdentity(jobClass.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    // todo: either make completely generic OR account for different Job classes.
    public Trigger buildTrigger(final Class jobClass, final TravelData travelData) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity("cool name", "travelJobs")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .startAt(new Date(System.currentTimeMillis()))
                .build();
    }

}
