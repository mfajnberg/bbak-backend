package de.mfberg.bbak.model;

import de.mfberg.bbak.model.adventuremap.HexTile;
import de.mfberg.bbak.model.creature.CreatureBase;
import de.mfberg.bbak.model.item.ItemBase;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Party {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private HexTile location;
    @OneToOne
    private HexTile destination;

    @OneToOne
    private CreatureBase leader;
}
