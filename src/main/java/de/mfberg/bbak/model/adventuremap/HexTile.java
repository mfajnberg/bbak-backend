package de.mfberg.bbak.model.adventuremap;

import de.mfberg.bbak.model.site.PlaceBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HexTile {
    @EmbeddedId
    private HexVector axial;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private PlaceBase place;
}
