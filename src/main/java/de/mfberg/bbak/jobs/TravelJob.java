package de.mfberg.bbak.jobs;

import de.mfberg.bbak.model.parties.Party;
import de.mfberg.bbak.model.worldmap.HexVector;
import de.mfberg.bbak.repo.HexRepository;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.services.parties.TravelService;
import lombok.Builder;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Builder
public class TravelJob implements Job {
    private final TravelService travelService;
    private final PartyRepository partyRepository;
    private final HexRepository hexRepository;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        TravelJobInfo jobInfo = (TravelJobInfo) dataMap.get("jobData");
        Party party = partyRepository.getReferenceById(jobInfo.getPartyId());
        List<HexVector> path = jobInfo.getPath();
        party.setLocation(hexRepository.getReferenceById(path.removeFirst()));
        if (!path.isEmpty()) {
            party.setDestination(hexRepository.getReferenceById(path.getFirst()));
            jobInfo.setLabel(travelService.makeTravelJobLabel(jobInfo));
            travelService.scheduleTravelJob(TravelJob.class, jobInfo);
        } else {
            party.setDestination(null);
        }
    }
}
