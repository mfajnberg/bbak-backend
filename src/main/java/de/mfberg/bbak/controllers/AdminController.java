package de.mfberg.bbak.controllers;

import de.mfberg.bbak.services.admin.AccountService;
import de.mfberg.bbak.services.admin.WorldmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final WorldmapService worldmapService;
    private final AccountService accountService;

    @GetMapping("/worldmap")
    public ResponseEntity<String> getWorldmap() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @PostMapping("/worldmap")
    public ResponseEntity<String> editWorldmap() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
