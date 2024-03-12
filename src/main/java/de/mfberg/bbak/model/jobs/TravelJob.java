package de.mfberg.bbak.model.jobs;

import de.mfberg.bbak.ApplicationContextProvider;
import de.mfberg.bbak.repo.PartyRepository;
import lombok.Builder;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Builder
public class TravelJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(TravelJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        PartyRepository partyRepository = applicationContext.getBean(PartyRepository.class);

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobInfo jobInfo = (JobInfo) jobDataMap.get(TravelJob.class.getSimpleName());
        LOG.info("~~~> traveling......." + jobInfo.getCallbackData());
    }
}
