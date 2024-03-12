package de.mfberg.bbak.model.site;

import de.mfberg.bbak.model.adventuremap.HexTile;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SiteBase {
    @Id
    private Long id;
    @OneToOne
    private HexTile location;
}
