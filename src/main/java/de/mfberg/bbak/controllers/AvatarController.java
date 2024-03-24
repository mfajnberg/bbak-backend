package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.CreatureDTO;
import de.mfberg.bbak.services.authentication.JwtService;
import de.mfberg.bbak.services.avatar.AvatarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/avatar")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService service;
    @PostMapping("/create")
    public ResponseEntity<?> createAvatar(HttpServletRequest request, @RequestBody CreatureDTO newAvatar) {
        try {
            service.createAvatar(request, newAvatar);
            return ResponseEntity.ok().body("Avatar creation successful.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
