package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.creature.CreatureBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatureRepository extends JpaRepository<CreatureBase, Long> {
}
