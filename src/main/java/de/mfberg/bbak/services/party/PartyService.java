package de.mfberg.bbak.services.party;

import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.jobs.TravelJob;
import de.mfberg.bbak.jobs.JobInfo;
import de.mfberg.bbak.services.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final SchedulerService scheduler;

    public void runTravelJob(TravelRequest request) {
        final JobInfo jobInfo = JobInfo.builder()
                .totalFireCount(5)
                .repeatIntervalMs(2000)
                .initialOffsetMs(1000)
                .callbackData("party x traveling to y")
                .build();
        scheduler.schedule(TravelJob.class, jobInfo);
    }
}
