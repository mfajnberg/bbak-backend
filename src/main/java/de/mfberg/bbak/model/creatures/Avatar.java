package de.mfberg.bbak.model.creatures;

import de.mfberg.bbak.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
public class Avatar extends CreatureBase {
    @OneToOne
    private User owner;
    @Enumerated(EnumType.STRING)
    private final CreatureType creatureType = CreatureType.AVATAR;
}
