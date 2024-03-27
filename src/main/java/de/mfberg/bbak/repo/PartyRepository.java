package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.parties.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
