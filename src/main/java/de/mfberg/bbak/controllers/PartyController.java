package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.PartyDTO;
import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.services.parties.PartyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/party")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService service;

    @GetMapping("/get")
    public ResponseEntity<?> getParty(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(service.getParty(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createParty(HttpServletRequest request, @RequestBody PartyDTO partyData) {
        try {
            service.createParty(request, partyData);
            return ResponseEntity.ok("Party creation successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/travel")
    public ResponseEntity<String> travel(HttpServletRequest request, @RequestBody TravelRequest travelRequest) {
        try {
            service.beginTravel(request, travelRequest);
            return ResponseEntity.ok("Travel start successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
