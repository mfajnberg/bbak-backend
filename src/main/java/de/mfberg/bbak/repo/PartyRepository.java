package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
