package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.PartyDTO;
import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.services.party.PartyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/party")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService service;

    @PostMapping("/create")
    public ResponseEntity<String> createParty(HttpServletRequest request, @RequestBody PartyDTO partyData) {
        try {
            service.createParty(request, partyData);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/travel")
    public ResponseEntity<String> travel(@RequestBody TravelRequest request) {
        service.runTravelJob(request);
        return ResponseEntity.ok("");
    }
}
