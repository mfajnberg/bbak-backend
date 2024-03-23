package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.HexTileDTO;
import de.mfberg.bbak.services.admin.AccountService;
import de.mfberg.bbak.services.admin.WorldmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final WorldmapService worldmapService;
    private final AccountService accountService;

    @GetMapping("/worldmap")
    public ResponseEntity<?> getWorldmap(
            @RequestParam(defaultValue = "0") Integer aroundAxialQ,
            @RequestParam(defaultValue = "0") Integer aroundAxialR,
            @RequestParam(defaultValue = "3") byte radius
    ) {
        return ResponseEntity.ok(worldmapService.getHexTiles(aroundAxialQ, aroundAxialR, radius));
    }

    @PostMapping("/worldmap")
    public ResponseEntity<String> editWorldmap(@RequestBody List<HexTileDTO> worldGenData) {
        try {
            worldmapService.editWorldmap(worldGenData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok("World edit successful.");
    }

    @DeleteMapping("/account")
    public ResponseEntity<String> deleteUserAccount() {
        return ResponseEntity.ok("User account deletion successful.");
    }
}
