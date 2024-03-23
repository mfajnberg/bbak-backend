package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.adventuremap.HexVector;
import de.mfberg.bbak.model.site.PlaceBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<PlaceBase, HexVector> {

}
