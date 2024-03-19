package de.mfberg.bbak.services.party;

import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.quartz.jobs.TravelJob;
import de.mfberg.bbak.quartz.jobs.TravelJobInfo;
import de.mfberg.bbak.quartz.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final SchedulerService scheduler;

    public void runTravelJob(TravelRequest request) {
        final TravelJobInfo travelJobInfo = TravelJobInfo.builder()
                .totalFireCount(5)
                .repeatIntervalMs(2000)
                .initialOffsetMs(1000)
                .callbackData("party x traveling to y")
                .build();
        scheduler.schedule(TravelJob.class, travelJobInfo);
    }
}
