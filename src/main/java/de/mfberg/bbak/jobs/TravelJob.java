package de.mfberg.bbak.jobs;

import de.mfberg.bbak.model.worldmap.HexVector;
import de.mfberg.bbak.ApplicationContextProvider;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.services.SchedulerService;
import de.mfberg.bbak.services.parties.TravelService;
import lombok.Builder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Builder
public class TravelJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(TravelService.class);
    private final TravelService travelService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        PartyRepository partyRepository = applicationContext.getBean(PartyRepository.class);

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        TravelData travelData = (TravelData) dataMap.get("jobData");
        List<HexVector> path = travelData.getPath();

        // todo: update party location, etc.

        if (path != null && path.size() > 1) {
            HexVector newLocation = path.removeFirst();
            HexVector newDestination = path.getFirst();
            travelService.schedule(TravelJob.class, travelData);
        }
    }
}
