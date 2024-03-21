package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.CreateAvatarRequest;
import de.mfberg.bbak.services.avatar.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/avatar")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService service;
    @PostMapping("/create")
    public ResponseEntity<?> createAvatar(CreateAvatarRequest newAvatar) {
        try {
            return ResponseEntity.ok(service.createAvatar(newAvatar));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Received invalid avatar data.");
        }
    }
}
