package de.mfberg.bbak.services.admin;

import de.mfberg.bbak.dto.HexTileDTO;
import de.mfberg.bbak.model.site.Forest;
import de.mfberg.bbak.model.site.PlaceBase;
import de.mfberg.bbak.model.site.PlaceType;
import de.mfberg.bbak.repo.HexRepository;
import de.mfberg.bbak.model.adventuremap.HexTile;
import de.mfberg.bbak.model.adventuremap.HexVector;
import de.mfberg.bbak.repo.PlaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorldmapService {
    private final HexRepository hexRepository;
    private final PlaceRepository placeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<HexTile> getHexTiles(Integer aroundAxialQ, Integer aroundAxialR, byte radius) {
        Set<HexVector> vectors = makeGridVectors(aroundAxialQ, aroundAxialR, radius);
        return hexRepository.findAllById(vectors);
    }

    @Transactional
    public void editWorldmap(List<HexTileDTO> worldGenData) {
        worldGenData.forEach(hexDTO -> {
            HexVector hexVector = new HexVector(hexDTO.getAxial().getQ(), hexDTO.getAxial().getR());
            PlaceBase newPlace = createPlace(hexDTO.getPlaceType());
            Optional<HexTile> dbHex = hexRepository.findByAxial(hexVector);
            dbHex.ifPresentOrElse(existingHex -> { // A HexTile exists, possibly with an associated place
                PlaceBase existingPlace = existingHex.getPlace();
                if (existingPlace != null) { // `existingHex` contains an `existingPlace`
                    existingHex.setPlace(null); // Set the place reference to null in `HexTile`
                    hexRepository.save(existingHex); // Save `HexTile` to persist the change
                    placeRepository.delete(existingPlace); // Delete the place safely
                }
                if (newPlace != null) { // `placeType` should be anything but `NONE`
                    existingHex.setPlace(newPlace);
                    hexRepository.save(existingHex);
                    newPlace.setLocation(existingHex);
                    placeRepository.save(newPlace);
                }
            }, () -> { // There's no `HexTile` at the given coordinates, consequently there's no `PlaceBase` either
                HexTile newHex = new HexTile();
                newHex.setAxial(hexVector);
                hexRepository.save(newHex);
                entityManager.flush();
                if (newPlace != null) { // `placeType` was anything but `NONE` or null
                    newPlace.setLocation(newHex);
                    placeRepository.save(newPlace);
                    newHex.setPlace(newPlace);
                }
            });
        });
    }
    private PlaceBase createPlace(PlaceType placeType) {
        return switch (placeType) {
            case NONE -> null;
            case FOREST -> new Forest();
            default -> throw new IllegalArgumentException("Unknown SiteType: " + placeType);
        };
    }

    private Set<HexVector> makeGridVectors(Integer aroundAxialQ, Integer aroundAxialR, byte radius) {
            Set<HexVector> result = new HashSet<HexVector>();
            result.add(new HexVector(0, 0));
            for (int currentRing = 1; currentRing <= radius; currentRing++)
            {
                HexVector vectorNE = new HexVector(currentRing, -1 * currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(
                            vectorNE.getQ() + aroundAxialQ, i + vectorNE.getR() + aroundAxialR));

                HexVector vectorE = new HexVector(currentRing, 0);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(
                            -1 * i + vectorE.getQ() + aroundAxialQ, i + vectorE.getR() + aroundAxialR));

                HexVector vectorSE = new HexVector(0, currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(
                            -1 * i + vectorSE.getQ() + aroundAxialQ, vectorSE.getR() + aroundAxialR));

                HexVector vectorSW = new HexVector(-1 * currentRing, currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(
                            vectorSW.getQ() + aroundAxialQ, -1 * i + vectorSW.getR() + aroundAxialR));

                HexVector vectorW = new HexVector(-1 * currentRing, 0);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(
                            i + vectorW.getQ() + aroundAxialQ, -1 * i + vectorW.getR() + aroundAxialR));

                HexVector vectorNW = new HexVector(0, -1 * currentRing);
                for (int i = 0; i < currentRing; i++)
                    result.add(new HexVector(
                            i + vectorNW.getQ() + aroundAxialQ, vectorNW.getR() + aroundAxialR));
            }
            return result;
    }
}
