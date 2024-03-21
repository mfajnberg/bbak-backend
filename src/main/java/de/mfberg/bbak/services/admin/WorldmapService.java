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

import java.util.ArrayList;
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

    private List<HexVector> makeGridVectors(byte radius) {
        {
            List<HexVector> result = new ArrayList<HexVector>();
            result.add(new HexVector(0, 0));
            for (int currentRing = 1; currentRing <= radius; currentRing++)
            {
                HexVector vectorNE = new HexVector(currentRing, -1 * currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(vectorNE.getQ(), i + vectorNE.getR()));

                HexVector vectorE = new HexVector(currentRing, 0);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(-1 * i + vectorE.getQ(), i + vectorE.getR()));

                HexVector vectorSE = new HexVector(0, currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(-1 * i + vectorSE.getQ(), vectorSE.getR()));

                HexVector vectorSW = new HexVector(-1 * currentRing, currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(vectorSW.getQ(), -1 * i + vectorSW.getR()));

                HexVector vectorW = new HexVector(-1 * currentRing, 0);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(i + vectorW.getQ(), -1 * i + vectorW.getR()));

                HexVector vectorNW = new HexVector(0, -1 * currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(i + vectorNW.getQ(), vectorNW.getR()));
            }
            return result;
        }
    }
}
