package de.mfberg.bbak.services.assets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AssetService {
    private final Path assetsLocation = Paths.get("src/main/resources/streamed");

    public Resource loadAsResource(String filename) throws Exception {
        Path file = assetsLocation.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new Exception("File not found " + filename);
        }
    }

    public String getContentType(String filename) {
        return MediaTypeFactory.getMediaType(filename).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
    }
}