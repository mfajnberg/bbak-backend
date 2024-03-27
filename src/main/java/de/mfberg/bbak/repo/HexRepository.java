package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.worldmap.HexTile;
import de.mfberg.bbak.model.worldmap.HexVector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HexRepository extends JpaRepository<HexTile, HexVector> {
    Optional<HexTile> findByAxial(HexVector coordinates);
}
