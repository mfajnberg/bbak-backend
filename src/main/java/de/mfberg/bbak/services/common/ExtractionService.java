package de.mfberg.bbak.services.common;

import de.mfberg.bbak.exceptions.ResourceNotFoundException;
import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.parties.Party;
import de.mfberg.bbak.model.user.User;
import de.mfberg.bbak.repo.CreatureRepository;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExtractionService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CreatureRepository creatureRepository;
    private final PartyRepository partyRepository;

    public User userFromClaim(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty())
            throw new ResourceNotFoundException("Error retrieving user from claim.");

        return user.get();
    }

    public Avatar avatarFromClaim(HttpServletRequest request) {
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

    public Party partyFromClaim(HttpServletRequest request) {
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
