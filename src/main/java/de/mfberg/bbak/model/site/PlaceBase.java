package de.mfberg.bbak.model.site;

import de.mfberg.bbak.model.adventuremap.HexTile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class PlaceBase {
    @Id
    @GeneratedValue
    private long id;
}
