package de.mfberg.bbak.model.creatures;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Pawn extends CreatureBase {
    @Enumerated(EnumType.STRING)
    private final CreatureType creatureType = CreatureType.PAWN;
}
