package de.mfberg.bbak.services.admin;

import de.mfberg.bbak.model.user.User;
import de.mfberg.bbak.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountMgmtService {
    private final UserRepository userRepository;
    public void deleteUser(String userEmail) throws Exception {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty())
            throw new Exception("User not found.");
        userRepository.delete(user.get());
    }
}
