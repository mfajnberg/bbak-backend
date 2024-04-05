package de.mfberg.bbak.model.parties;

import de.mfberg.bbak.model.worldmap.HexTile;
import de.mfberg.bbak.model.creatures.CreatureBase;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Party {
    @Id
    @GeneratedValue
    private long id;
    @OneToOne
    private HexTile location;
    @OneToOne
    private HexTile destination;
    @OneToOne
    private CreatureBase leader;
}
