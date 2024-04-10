package de.mfberg.bbak.services.admin;

import de.mfberg.bbak.exceptions.ResourceNotFoundException;
import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.creatures.CreatureBase;
import de.mfberg.bbak.model.parties.Party;
import de.mfberg.bbak.model.user.User;
import de.mfberg.bbak.repo.CreatureRepository;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountAdminService {
    private final UserRepository userRepository;
    private final CreatureRepository creatureRepository;
    private final PartyRepository partyRepository;

    public void deleteAccount(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty())
            throw new ResourceNotFoundException("User not found.");

        Optional<Avatar> avatar = creatureRepository.findAvatarByOwner(user.get());
        if (avatar.isPresent()) {
            Optional<Party> party = partyRepository.findPartyByLeader(avatar.get());
            if (party.isPresent()) {
                List<CreatureBase> members = creatureRepository.findAllPartyMembers(party.get().getId());
                members.forEach(creatureRepository::delete);
                partyRepository.delete(party.get());
                // todo: also delete items from backpacks
            }
            creatureRepository.delete(avatar.get());
        }
        userRepository.delete(user.get());
    }
}
