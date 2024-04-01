package de.mfberg.bbak.services;

import de.mfberg.bbak.jobs.TravelJobInfo;
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
public class QuartzService {
    private static final Logger LOG = LoggerFactory.getLogger(QuartzService.class);
    private final Scheduler scheduler;

    public Scheduler getScheduler() {
        return scheduler;
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public JobDetail buildJobDetail(final Class jobClass, final TravelJobInfo jobInfo) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobData", jobInfo);

        return JobBuilder
                .newJob(jobClass)
                // todo: identity should associate the party and 'handle' collisions.
                .withIdentity(jobInfo.getLabel())
                .setJobData(jobDataMap)
                .build();
    }

    // todo: either make completely generic OR account for different Job classes.
    public Trigger buildTrigger(final Class jobClass, final TravelJobInfo jobInfo) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobInfo.getLabel(), jobInfo.getGroupLabel())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .startAt(new Date(System.currentTimeMillis() + jobInfo.getDurationMillis()))
                .build();
    }

}
