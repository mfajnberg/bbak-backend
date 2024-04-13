package de.mfberg.bbak.services.creatures;

import de.mfberg.bbak.dto.CreatureDTO;
import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.user.User;
import de.mfberg.bbak.repo.CreatureRepository;
import de.mfberg.bbak.services.common.ExtractionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final CreatureRepository creatureRepository;
    private final ExtractionService extractionService;

    public void createAvatar(HttpServletRequest request, CreatureDTO newAvatarData) {
        // todo: Prüfung der Charakterwerte im DTO bezüglich Einhaltung von Spielregeln
        Avatar avatar = (Avatar) new CreatureFactory().fromDTO(newAvatarData);
        User user = extractionService.userFromClaim(request);
        avatar.setOwner(user);
        creatureRepository.save(avatar);
    }
}
