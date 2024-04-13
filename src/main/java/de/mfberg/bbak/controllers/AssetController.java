package de.mfberg.bbak.controllers;

import de.mfberg.bbak.services.assets.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService service;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveAsset(@PathVariable String filename) {
            Resource resource = service.loadAsResource(filename);
            String contentType = service.getContentType(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
    }
}