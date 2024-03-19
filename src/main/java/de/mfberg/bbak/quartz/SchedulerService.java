package de.mfberg.bbak.quartz;

import de.mfberg.bbak.quartz.jobs.TravelJobInfo;
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
public class SchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;

    public void schedule(final Class jobClass, final TravelJobInfo travelJobInfo) {
        try {
            scheduler.scheduleJob(
                    buildJobDetail(jobClass, travelJobInfo),
                    buildTrigger(jobClass, travelJobInfo));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public List<TravelJobInfo> getAllRunningJobs() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        try {
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            return (TravelJobInfo) jobDetail.getJobDataMap().get(jobKey.getName());
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

    public TravelJobInfo getRunningJob(String jobId) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobId));
            if (jobDetail == null)
                return null;
            return (TravelJobInfo) jobDetail.getJobDataMap().get(jobId);
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

    public static JobDetail buildJobDetail(final Class jobClass, final TravelJobInfo travelJobInfo) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(), travelJobInfo);

        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final Class jobClass, final TravelJobInfo travelJobInfo) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(travelJobInfo.getRepeatIntervalMs());

        if (travelJobInfo.isRunForever()) {
            builder = builder.repeatForever();
        } else {
            builder = builder.withRepeatCount(travelJobInfo.getTotalFireCount() - 1);
        }

        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobClass.getSimpleName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis() + travelJobInfo.getInitialOffsetMs()))
                .build();
    }
}