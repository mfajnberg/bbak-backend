package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.creatures.CreatureBase;
import de.mfberg.bbak.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CreatureRepository extends JpaRepository<CreatureBase, Long> {
    Optional<Avatar> findAvatarByOwner(User user);
    @Query("""
        select c from CreatureBase c inner join Party p on c.party.id = p.id
        where p.id = :partyId
    """)
    List<CreatureBase> findAllPartyMembers(Long partyId);
}
