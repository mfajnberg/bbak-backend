package de.mfberg.bbak.services.admin;

import de.mfberg.bbak.dto.HexTileDTO;
import de.mfberg.bbak.model.site.Forest;
import de.mfberg.bbak.model.site.SiteBase;
import de.mfberg.bbak.model.site.SiteType;
import de.mfberg.bbak.repo.WorldRepository;
import de.mfberg.bbak.model.adventuremap.HexTile;
import de.mfberg.bbak.model.adventuremap.HexVector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WorldmapService {
    private final WorldRepository worldRepository;
    public List<HexTile> getHexTiles(Set<HexVector> vectors) {
        return worldRepository.findAllById(vectors);
    }

    public void editWorldmap(List<HexTileDTO> worldGenData) {
        worldGenData.forEach(hexDTO -> {
            HexTile newHex = HexTile.builder()
                    .axial(new HexVector(
                            hexDTO.getAxial().getQ(),
                            hexDTO.getAxial().getR()))
                    .site(createSite(hexDTO.getSiteType())) // todo: HUH????
                    .build();
            Optional<HexTile> dbHex = worldRepository.findByAxial(hexDTO.getAxial());
            dbHex.ifPresentOrElse(existingHex -> {
                existingHex.setSite(newHex.getSite());
                worldRepository.save(existingHex);
            }, () -> {
                worldRepository.save(newHex);
            });
        });
    }

    private SiteBase createSite(SiteType type) {
        return switch (type) {
            case NONE -> null;
            case FOREST -> new Forest();
            default -> throw new IllegalArgumentException("Unknown SiteType: " + type);
        };
    }
}
