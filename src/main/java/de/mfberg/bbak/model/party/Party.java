package de.mfberg.bbak.model.party;

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
    private long id;
    @OneToOne
    private HexTile location;

    @OneToOne
    private CreatureBase leader;
    @OneToMany
    private List<CreatureBase> members;
}
