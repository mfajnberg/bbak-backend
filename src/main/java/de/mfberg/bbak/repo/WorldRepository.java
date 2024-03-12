package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.adventuremap.HexTile;
import de.mfberg.bbak.model.adventuremap.HexVector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorldRepository extends JpaRepository<HexTile, HexVector> {
    Optional<HexTile> findByAxial(HexVector coordinates);
}
