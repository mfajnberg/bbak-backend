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
    public ResponseEntity<PartyDTO> getParty(HttpServletRequest request) {
        return ResponseEntity.ok(service.getParty(request));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createParty(HttpServletRequest request, @RequestBody PartyDTO partyData) {
        service.createParty(request, partyData);
        return ResponseEntity.ok("Party creation successful.");
    }

    @PostMapping("/travel")
    public ResponseEntity<String> travel(HttpServletRequest request, @RequestBody TravelRequest travelRequest) {
        service.beginTravel(request, travelRequest);
        return ResponseEntity.ok("Travel start successful.");
    }
}
