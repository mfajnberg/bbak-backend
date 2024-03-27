package de.mfberg.bbak.jobs;

import de.mfberg.bbak.model.adventuremap.HexVector;
import de.mfberg.bbak.ApplicationContextProvider;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.services.party.TravelJobService;
import lombok.Builder;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Builder
public class TravelJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        PartyRepository partyRepository = applicationContext.getBean(PartyRepository.class);
        TravelJobService travelJobService = applicationContext.getBean(TravelJobService.class);

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        TravelData travelData = (TravelData) dataMap.get("jobData");
        List<HexVector> path = travelData.getPath();

        // todo: update party location, etc.

        if (path != null && !path.isEmpty()) {
            HexVector newLocation = path.removeFirst();
            HexVector newDestination = path.getFirst();
            travelJobService.schedule(TravelJob.class, travelData);
        }
    }

    // Beispiel-Methoden für Serialisierung und Deserialisierung

}
