package de.mfberg.bbak.services.party;

import de.mfberg.bbak.jobs.TravelData;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TravelJobService {
    private static final Logger LOG = LoggerFactory.getLogger(TravelJobService.class);
    private final Scheduler scheduler;

    public void schedule(final Class jobClass, final TravelData travelData) {
        try {
            scheduler.scheduleJob(
                    buildJobDetail(jobClass, travelData),
                    buildTrigger(jobClass, travelData));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public List<TravelData> getAllRunningJobs() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        try {
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            return (TravelData) jobDetail.getJobDataMap().get(jobKey.getName());
                        } catch (SchedulerException e) {
                            LOG.error(e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public TravelData getRunningJob(String jobId) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobId));
            if (jobDetail == null)
                return null;
            return (TravelData) jobDetail.getJobDataMap().get(jobId);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
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

    private JobDetail buildJobDetail(final Class jobClass, final Object jobData) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobData", jobData);

        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    private Trigger buildTrigger(final Class jobClass, final TravelData travelData) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity("cool name", "travelJobs")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .startAt(new Date(System.currentTimeMillis()))
                .build();
    }
}
