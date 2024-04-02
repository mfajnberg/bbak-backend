package de.mfberg.bbak.services.admin;

import de.mfberg.bbak.dto.HexTileDTO;
import de.mfberg.bbak.model.places.Forest;
import de.mfberg.bbak.model.places.PlaceBase;
import de.mfberg.bbak.model.places.PlaceType;
import de.mfberg.bbak.repo.HexRepository;
import de.mfberg.bbak.model.worldmap.HexTile;
import de.mfberg.bbak.model.worldmap.HexVector;
import de.mfberg.bbak.repo.PlaceRepository;
import de.mfberg.bbak.services.places.PlaceFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorldEditService {
    private final HexRepository hexRepository;
    private final PlaceRepository placeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<HexTile> getHexTiles(Integer aroundAxialQ, Integer aroundAxialR, byte radius) {
        Set<HexVector> vectors = HexVector.makeGrid(aroundAxialQ, aroundAxialR, radius);
        return hexRepository.findAllById(vectors);
    }

    public List<HexTileDTO> getHexTileDTOs(Integer aroundAxialQ, Integer aroundAxialR, byte radius) {
        Set<HexVector> vectors = HexVector.makeGrid(aroundAxialQ, aroundAxialR, radius);
        List<HexTileDTO> result = new ArrayList<HexTileDTO>();
        vectors.forEach(axial -> {
            HexTileDTO hexDTO = new HexTileDTO(axial, null);
            Optional<PlaceBase> place = placeRepository.findPlaceByHexVector(axial.getQ(), axial.getR());
            place.ifPresent(existingPlace -> {
                hexDTO.setPlaceType(determinePlaceType(existingPlace));
            });
            result.add(hexDTO);
        });
        return result;
    }

    @Transactional
    public void editWorldmap(List<HexTileDTO> worldGenData) {
        worldGenData.forEach(hexDTO -> {
            HexVector hexVector = new HexVector(hexDTO.getAxial().getQ(), hexDTO.getAxial().getR());
            PlaceBase newPlace = new PlaceFactory().fromPlaceType(hexDTO.getPlaceType());
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
                    placeRepository.save(newPlace);
                }
            }, () -> { // There's no `HexTile` at the given coordinates, consequently there's no `PlaceBase` either
                HexTile newHex = new HexTile();
                newHex.setAxial(hexVector);
                hexRepository.save(newHex);
                entityManager.flush();
                if (newPlace != null) { // `placeType` was anything but `NONE` or null
                    placeRepository.save(newPlace);
                    newHex.setPlace(newPlace);
                    hexRepository.save(newHex);
                }
            });
        });
    }

    private PlaceType determinePlaceType(PlaceBase place) {
        if (place instanceof Forest) return PlaceType.FOREST;
        // else if (place instanceof SUBCLASS) return PlaceType.TYPE;
        // ...
        else return PlaceType.NONE;
    }
}
