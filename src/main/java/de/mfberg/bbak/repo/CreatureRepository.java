package de.mfberg.bbak.repo;

import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.creatures.CreatureBase;
import de.mfberg.bbak.model.places.PlaceBase;
import de.mfberg.bbak.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreatureRepository extends JpaRepository<CreatureBase, Long> {
    Optional<Avatar> findAvatarByOwner(User user);
}
