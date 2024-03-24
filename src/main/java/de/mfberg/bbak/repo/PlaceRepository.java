package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.places.PlaceBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<PlaceBase, Long> {
    @Query("SELECT p FROM HexTile h JOIN h.place p WHERE h.axial.q = :q AND h.axial.r = :r")
    Optional<PlaceBase> findPlaceByHexVector(@Param("q") Long q, @Param("r") Long r);
}

