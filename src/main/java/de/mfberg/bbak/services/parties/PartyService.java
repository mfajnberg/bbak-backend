package de.mfberg.bbak.services.parties;

import de.mfberg.bbak.dto.PartyDTO;
import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.creatures.CreatureBase;
import de.mfberg.bbak.model.parties.Party;
import de.mfberg.bbak.model.user.User;
import de.mfberg.bbak.repo.CreatureRepository;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.repo.UserRepository;
import de.mfberg.bbak.services.SchedulerService;
import de.mfberg.bbak.services.authentication.JwtService;
import de.mfberg.bbak.services.creatures.CreatureFactory;
import de.mfberg.bbak.jobs.TravelJob;
import de.mfberg.bbak.jobs.TravelData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final UserRepository userRepository;
    private final CreatureRepository creatureRepository;
    private final PartyRepository partyRepository;
    private final JwtService jwtService;
    private final TravelService travelService;
    private static final Logger LOG = LoggerFactory.getLogger(TravelService.class);

    public void beginTravel(TravelRequest travelRequest) {
        final TravelData travelData = TravelData.builder()
                .path(travelRequest.getPath())
                // todo: set a travel speed, etc.
                .build();
        travelService.schedule(TravelJob.class, travelData);
    }

    public void createParty(HttpServletRequest request, PartyDTO partyData) throws Exception {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty())
            throw new Exception("Error retrieving user from claim.");
        Optional<Avatar> leader = creatureRepository.findAvatarByOwner(user.get());
        // todo: instead, fetch by `partyData.leader.name` and validate owner
        if (leader.isEmpty())
            throw new Exception("Please create an avatar before creating a party.");
        Party newParty = new Party();
        newParty.setLeader(leader.get());
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
}
