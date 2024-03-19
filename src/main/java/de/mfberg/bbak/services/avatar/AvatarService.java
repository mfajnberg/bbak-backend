package de.mfberg.bbak.services.avatar;

import de.mfberg.bbak.repo.CreatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final CreatureRepository creatureRepository;
}
