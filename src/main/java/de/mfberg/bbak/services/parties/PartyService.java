package de.mfberg.bbak.services.parties;

import de.mfberg.bbak.dto.CreatureDTO;
import de.mfberg.bbak.dto.PartyDTO;
import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.exceptions.JobConflictException;
import de.mfberg.bbak.exceptions.InvalidDataException;
import de.mfberg.bbak.exceptions.ResourceNotFoundException;
import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.creatures.CreatureBase;
import de.mfberg.bbak.model.parties.Party;
import de.mfberg.bbak.model.places.PlaceBase;
import de.mfberg.bbak.model.user.User;
import de.mfberg.bbak.model.worldmap.HexTile;
import de.mfberg.bbak.model.worldmap.HexVector;
import de.mfberg.bbak.repo.CreatureRepository;
import de.mfberg.bbak.repo.HexRepository;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.repo.UserRepository;
import de.mfberg.bbak.services.QuartzService;
import de.mfberg.bbak.services.authentication.JwtService;
import de.mfberg.bbak.services.creatures.CreatureFactory;
import de.mfberg.bbak.jobs.TravelJob;
import de.mfberg.bbak.jobs.TravelJobInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final UserRepository userRepository;
    private final CreatureRepository creatureRepository;
    private final PartyRepository partyRepository;
    private final HexRepository hexRepository;
    private final JwtService jwtService;
    private final TravelService travelService;
    private final QuartzService quartzService;

    @Transactional
    public PartyDTO getParty(HttpServletRequest request) {
        Party party;
        party = extractPartyFromClaim(request);


        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setLeader(new CreatureDTO(party.getLeader()));

        List<CreatureBase> members;
        members = creatureRepository.findAllPartyMembers(party.getId());
        partyDTO.setMembers(new ArrayList<>());
        members.forEach(member -> {
            partyDTO.getMembers().add(new CreatureDTO(member));
        });

        try {
            for (JobKey jobKey : quartzService.getScheduler().getJobKeys(GroupMatcher.anyGroup())) {
                if (jobKey.getName().contains(party.getId() + "=")) {
                    Scheduler scheduler = quartzService.getScheduler();
                    Trigger trigger = scheduler.getTriggersOfJob(jobKey).getFirst();

                    Date date = trigger.getStartTime();
                    partyDTO.setRemainingJobDurationMillis(date.getTime() - System.currentTimeMillis());
                    partyDTO.setDestinationRelative(new HexVector(
                            party.getDestination().getAxial().getQ() - party.getLocation().getAxial().getQ(),
                            party.getDestination().getAxial().getR() - party.getLocation().getAxial().getR()
                    ));
                    break; // todo: other job types
                }
            }
        } catch (Exception ignored) { }

        return partyDTO;
    }

    @Transactional
    public void beginTravel(HttpServletRequest request, TravelRequest travelRequest) {
        Party party = extractPartyFromClaim(request);

        Set<JobKey> jobKeys = new HashSet<>();
        try {
            jobKeys = quartzService.getScheduler().getJobKeys(GroupMatcher.anyGroup());
        } catch (Exception ignored) { }
        for (JobKey jobKey : jobKeys) {
            if (jobKey.getName().contains(party.getId() + "=")) {
                throw new JobConflictException("Failed to begin travel (party already travelling)");
            }
        }

        HexVector hexVecPrevious = null;
        List<HexTile> hexRefPath = new ArrayList<>();
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
            HexTile hexRef = null;
            try {
                hexRef = hexRepository.getReferenceById(hexVec);
            } catch (Exception e) {
                throw new InvalidDataException("Failed to begin travel (non-existent coordinates)");
            }
            if (hexRef == party.getLocation())
                throw new InvalidDataException("Failed to begin travel (walking in circles)");
            PlaceBase place = hexRef.getPlace();
            if (place != null && place.isBlocking())
                throw new InvalidDataException("Failed to begin travel (some tiles are blocked)");
            hexRefPath.add(hexRef);
            hexVecPrevious = hexVec;
        }

        final TravelJobInfo travelJobInfo = TravelJobInfo.builder()
                .partyId(party.getId())
                .path(travelRequest.getPath())
                .durationMillis(500000)
                .build();
        travelJobInfo.setLabel(travelService.makeLabel(travelJobInfo));
        travelService.schedule(TravelJob.class, travelJobInfo);
        party.setDestination(hexRefPath.getFirst());
    }

    public void createParty(HttpServletRequest request, PartyDTO partyData) {
        Avatar leader = extractAvatarFromClaim(request);
        Party newParty = new Party();
        newParty.setLeader(leader);
        newParty.setLocation(hexRepository.getReferenceById(new HexVector(0, 0)));
        partyRepository.save(newParty);
        partyData.getMembers().forEach(memberDTO -> {
            CreatureBase newMember = null;
            newMember = new CreatureFactory().fromDTO(memberDTO);
            newMember.setParty(newParty);
            creatureRepository.save(newMember);
        });
    }

    private Avatar extractAvatarFromClaim(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty())
            throw new ResourceNotFoundException("Error retrieving user from claim.");

        Optional<Avatar> leader = creatureRepository.findAvatarByOwner(user.get());
        if (leader.isEmpty())
            throw new ResourceNotFoundException("Error retrieving leader from claim.");

        return leader.get();
    }

    private Party extractPartyFromClaim(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty())
            throw new ResourceNotFoundException("Error retrieving user from claim.");

        Optional<Avatar> leader = creatureRepository.findAvatarByOwner(user.get());
        if (leader.isEmpty())
            throw new ResourceNotFoundException("Error retrieving leader from claim.");

        Optional<Party> party = partyRepository.findPartyByLeader(leader.get());
        if (party.isEmpty())
            throw new ResourceNotFoundException("Error retrieving party from claim.");

        return party.get();
    }

}
