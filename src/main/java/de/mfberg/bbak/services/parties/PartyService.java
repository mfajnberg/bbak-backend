package de.mfberg.bbak.services.parties;

import de.mfberg.bbak.dto.PartyDTO;
import de.mfberg.bbak.dto.TravelRequest;
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
import org.quartz.JobKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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

    public void beginTravel(HttpServletRequest request, TravelRequest travelRequest) throws Exception {
        Party party = extractPartyFromClaim(request);

        for (JobKey jobKey : quartzService.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals("travelJobs"))) {
            if (jobKey.getName().contains(party.getId() + ":")) {
                throw new Exception("Failed to begin travel (party already travelling)");
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
                    throw new RuntimeException("Failed to begin travel (recurring coordinates)");
                if (distance > 1)
                    throw new RuntimeException("Failed to begin travel (found skipping tiles)");
            }
            HexTile hexRef = null;
            try {
                hexRef = hexRepository.getReferenceById(hexVec);
            } catch (Exception e) {
                throw new RuntimeException("Failed to begin travel (non-existent coordinates)");
            }
            PlaceBase place = hexRef.getPlace();
            if (place != null && place.isBlocking())
                throw new RuntimeException("Failed to begin travel (some tiles are blocked)");
            hexRefPath.add(hexRef);
            hexVecPrevious = hexVec;
        }

        final TravelJobInfo travelJobInfo = TravelJobInfo.builder()
                .partyId(party.getId())
                .path(travelRequest.getPath())
                .durationMillis(50000)
                .build();
        travelJobInfo.setLabel(travelService.makeLabel(travelJobInfo));
        travelService.schedule(TravelJob.class, travelJobInfo);
    }

    public void createParty(HttpServletRequest request, PartyDTO partyData) throws Exception {
        Avatar leader = extractAvatarFromClaim(request);
        Party newParty = new Party();
        newParty.setLeader(leader);
        newParty.setLocation(hexRepository.getReferenceById(new HexVector(0, 0)));
        partyRepository.save(newParty);
        partyData.getMembers().forEach(memberDTO -> {
            CreatureBase newMember = null;
            try {
                newMember = new CreatureFactory().fromDTO(memberDTO);
                newMember.setParty(newParty);
                creatureRepository.save(newMember);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Avatar extractAvatarFromClaim(HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty())
            throw new Exception("Error retrieving user from claim.");

        Optional<Avatar> leader = creatureRepository.findAvatarByOwner(user.get());
        if (leader.isEmpty())
            throw new Exception("Error retrieving leader from claim.");

        return leader.get();
    }

    private Party extractPartyFromClaim(HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty())
            throw new Exception("Error retrieving user from claim.");

        Optional<Avatar> leader = creatureRepository.findAvatarByOwner(user.get());
        if (leader.isEmpty())
            throw new Exception("Error retrieving leader from claim.");

        Optional<Party> party = partyRepository.findPartyByLeader(leader.get());
        if (party.isEmpty())
            throw new Exception("Error retrieving party from claim.");

        return party.get();
    }
}
