package de.mfberg.bbak.services.avatar;

import de.mfberg.bbak.dto.CreatureDTO;
import de.mfberg.bbak.model.creature.Avatar;
import de.mfberg.bbak.model.user.User;
import de.mfberg.bbak.repo.CreatureRepository;
import de.mfberg.bbak.repo.UserRepository;
import de.mfberg.bbak.services.authentication.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final CreatureRepository creatureRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void createAvatar(HttpServletRequest request, CreatureDTO newAvatar) throws Exception {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty())
            throw new Exception("Error retrieving user from claim.");

        Avatar avatar = new Avatar();
        avatar.setOwner(user.get());
        avatar.setName(newAvatar.getName());
        avatar.setDescription(newAvatar.getDescription());
        avatar.setIntellect(newAvatar.getIntellect());
        avatar.setDiscipline(newAvatar.getDiscipline());
        avatar.setPower(newAvatar.getPower());
        avatar.setAgility(newAvatar.getAgility());
        avatar.setLucidity(newAvatar.getLucidity());
        avatar.setCharisma(newAvatar.getCharisma());
        avatar.setAlignment(newAvatar.getAlignment());
        avatar.setTemperament(newAvatar.getTemperament());
        avatar.setMorale(newAvatar.getMorale());
        creatureRepository.save(avatar);
    }
}
