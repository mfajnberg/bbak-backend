package de.mfberg.bbak.services.avatar;

import de.mfberg.bbak.dto.CreateAvatarRequest;
import de.mfberg.bbak.repo.CreatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final CreatureRepository creatureRepository;

    public CreateAvatarRequest createAvatar(CreateAvatarRequest newAvatar) {
        // build avatar creature with param
        // build party with param
        // persist avatar and his party
        // return party dto
        return new CreateAvatarRequest();
    }
}
