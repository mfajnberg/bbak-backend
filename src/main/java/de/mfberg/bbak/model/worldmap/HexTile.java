package de.mfberg.bbak.model.worldmap;

import de.mfberg.bbak.model.places.PlaceBase;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class HexTile {
    @EmbeddedId
    private HexVector axial;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "place_id")
    private PlaceBase place;
}
