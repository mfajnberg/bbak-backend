package de.mfberg.bbak.model.site;

import de.mfberg.bbak.model.adventuremap.HexTile;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class PlaceBase {
    @Id
    @GeneratedValue
    private long id;
    @OneToOne(mappedBy = "place")
    private HexTile location;
}
