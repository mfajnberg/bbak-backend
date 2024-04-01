package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.HexTileDTO;
import de.mfberg.bbak.services.QuartzService;
import de.mfberg.bbak.services.admin.AccountService;
import de.mfberg.bbak.services.admin.WorldmapService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final WorldmapService worldmapService;
    private final QuartzService quartzService;

    @GetMapping("/worldmap")
    public ResponseEntity<?> getWorldmap(
            @RequestParam(defaultValue = "0") Integer aroundAxialQ,
            @RequestParam(defaultValue = "0") Integer aroundAxialR,
            @RequestParam(defaultValue = "3") byte radius
    ) {
            return ResponseEntity.ok(worldmapService.getHexTileDTOs(aroundAxialQ, aroundAxialR, radius));
    }

    @PostMapping("/worldmap")
    public ResponseEntity<String> editWorldmap(@RequestBody List<HexTileDTO> worldGenData) {
        try {
            worldmapService.editWorldmap(worldGenData);
            return ResponseEntity.ok("World edit successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/account")
    public ResponseEntity<String> deleteUserAccount() {
        // todo: implement user account deletion endpoint
        return ResponseEntity.ok("User account deletion successful.");
    }

    @GetMapping("/jobs")
    public Map<String, String> getScheduledJobs() {
        Map<String, String> scheduledJobs = new HashMap<>();
        Scheduler scheduler = quartzService.getScheduler();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger : triggers) {
                        if (trigger.getNextFireTime() != null) {
                            scheduledJobs.put(jobKey.toString(), trigger.getNextFireTime().toString());
                        }
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Map.of("Fehler", "Fehler beim Abrufen der geplanten Jobs.");
        }
        return scheduledJobs;
    }
}
