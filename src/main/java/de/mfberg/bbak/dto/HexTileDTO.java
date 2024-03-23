package de.mfberg.bbak.dto;

import de.mfberg.bbak.model.adventuremap.HexVector;
import de.mfberg.bbak.model.site.PlaceType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HexTileDTO {
    private HexVector axial;
    private PlaceType placeType;
}
