package de.mfberg.bbak.controllers;

import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.services.party.PartyService;
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

    @PostMapping("/travel")
    public ResponseEntity<String> travel(@RequestBody TravelRequest request) {
        service.runTravelJob(request);
        return ResponseEntity.ok("");
    }
}
