package de.mfberg.bbak.services.assets;

import de.mfberg.bbak.exceptions.ResourceLoadingException;
import de.mfberg.bbak.exceptions.ResourceNotFoundException;
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

    public Resource loadAsResource(String filename) {
        try {
            Path file = assetsLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable())
                return resource;
            else {
                throw new ResourceNotFoundException("File not found: " + filename);
            }
        } catch (Exception e) {
            throw new ResourceLoadingException("Could not load file: " + filename, e);
        }
    }

    public String getContentType(String filename) {
        return MediaTypeFactory.getMediaType(filename).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
    }
}