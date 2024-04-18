package de.mfberg.bbak.services.parties;

import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.exceptions.InvalidDataException;
import de.mfberg.bbak.exceptions.JobConflictException;
import de.mfberg.bbak.exceptions.JobSchedulingException;
import de.mfberg.bbak.jobs.TravelJob;
import de.mfberg.bbak.jobs.TravelJobInfo;
import de.mfberg.bbak.model.parties.Party;
import de.mfberg.bbak.model.places.PlaceBase;
import de.mfberg.bbak.model.worldmap.HexTile;
import de.mfberg.bbak.model.worldmap.HexVector;
import de.mfberg.bbak.repo.HexRepository;
import de.mfberg.bbak.services.common.ExtractionService;
import de.mfberg.bbak.services.common.QuartzService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class TravelService {
    private static final Logger LOG = LoggerFactory.getLogger(TravelService.class);
    private final HexRepository hexRepository;
    private final ExtractionService extractionService;
    private final QuartzService quartzService;

    @Transactional
    public void beginTravel(HttpServletRequest request, TravelRequest travelRequest) {
        Party party = extractionService.partyFromClaim(request);
        validateTravelRequest(travelRequest, party);

        final TravelJobInfo travelJobInfo = TravelJobInfo.builder()
                .partyId(party.getId())
                .path(travelRequest.getPath())
                .durationMillis(5000)
                .build();

        travelJobInfo.setLabel(makeTravelJobLabel(travelJobInfo));
        scheduleTravelJob(TravelJob.class, travelJobInfo);
        party.setDestination(
                hexRepository.getReferenceById(
                        travelRequest.getPath().getFirst()));
    }

    private void validateTravelRequest(TravelRequest travelRequest, Party party) {
        validateTravelJobKey(party);
        validateTravelPath(travelRequest, party);
    }

    private void validateTravelJobKey(Party party) {
        Set<JobKey> jobKeys = new HashSet<>();
        try {
            jobKeys = quartzService.getScheduler().getJobKeys(GroupMatcher.anyGroup());
        } catch (Exception ignored) { }
        for (JobKey jobKey : jobKeys) {
            if (jobKey.getName().contains(party.getId() + "~")) {
                throw new JobConflictException("Failed to begin travel (party already travelling)");
            }
        }
    }

    private void validateTravelPath(TravelRequest travelRequest, Party party) {
        HexVector hexVecPrevious = null;
        for (HexVector hexVec : travelRequest.getPath()) {
            hexVec.setQ(hexVec.getQ() + party.getLocation().getAxial().getQ());
            hexVec.setR(hexVec.getR() + party.getLocation().getAxial().getR());
            if (hexVecPrevious != null) {
                long distance = HexVector.axialDistance(hexVecPrevious, hexVec);
                if (distance == 0)
                    throw new InvalidDataException("Failed to begin travel (recurring coordinates)");
                if (distance > 1)
                    throw new InvalidDataException("Failed to begin travel (found skipping tiles)");
            }
            HexTile hexRef;
            try {
                hexRef = hexRepository.getReferenceById(hexVec);
            } catch (Exception probablyAnEntityNotFoundException) {
                throw new InvalidDataException("Failed to begin travel (non-existent coordinates)");
            }
            if (hexRef == party.getLocation())
                throw new InvalidDataException("Failed to begin travel (walking in circles)");
            PlaceBase place = hexRef.getPlace();
            if (place != null && place.isBlocking())
                throw new InvalidDataException("Failed to begin travel (some tiles are blocked)");
            hexVecPrevious = hexVec;
        }
    }

    public void scheduleTravelJob(final Class jobClass, final TravelJobInfo travelJobInfo) {
        try {
            quartzService.getScheduler().scheduleJob(
                    quartzService.buildJobDetail(jobClass, travelJobInfo),
                    quartzService.buildTrigger(jobClass, travelJobInfo));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            throw new JobSchedulingException(jobClass.getSimpleName());
        }
    }

    public String makeTravelJobLabel(TravelJobInfo info) {
        return info.getPartyId() + "~>" +
                (info.getPath().getFirst().getQ()) + ":" +
                (info.getPath().getFirst().getR());
    }
}
