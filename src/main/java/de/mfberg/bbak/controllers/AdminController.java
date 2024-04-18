package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.HexTileDTO;
import de.mfberg.bbak.services.admin.AccountAdminService;
import de.mfberg.bbak.services.admin.JobAdminService;
import de.mfberg.bbak.services.admin.WorldAdminService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final WorldAdminService worldAdminService;
    private final AccountAdminService accountAdminService;
    private final JobAdminService jobAdminService;

    @GetMapping("/worldmap")
    public ResponseEntity<List<HexTileDTO>> getWorldmap(
            @RequestParam(defaultValue = "0") long aroundAxialQ,
            @RequestParam(defaultValue = "0") long aroundAxialR,
            @RequestParam(defaultValue = "3") byte radius
    ) {
            List<HexTileDTO> worldMap = worldAdminService.getHexTileDTOs(aroundAxialQ, aroundAxialR, radius);
            return ResponseEntity.ok(worldMap);
    }

    @PostMapping("/worldmap")
    public ResponseEntity<String> editWorldmap(@RequestBody List<HexTileDTO> worldGenData) {
            worldAdminService.editWorldmap(worldGenData);
            return ResponseEntity.ok("World edit successful.");
    }

    @DeleteMapping("/account/{userEmail:.+}")
    public ResponseEntity<String> deleteAccount(@PathVariable String userEmail) {
            accountAdminService.deleteAccount(userEmail);
            return ResponseEntity.ok("User account deletion successful.");
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> getScheduledJobs() {
        try {
            return ResponseEntity.ok(jobAdminService.getScheduledJobs());
        } catch (SchedulerException e) {
            return ResponseEntity.status(500).body("Error: Couldn't fetch jobs from quartz service.");
        }
    }
}
