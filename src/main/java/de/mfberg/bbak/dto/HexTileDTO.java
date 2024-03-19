package de.mfberg.bbak.dto;

import de.mfberg.bbak.model.adventuremap.HexVector;
import de.mfberg.bbak.model.site.SiteType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HexTileDTO {
    private HexVector axial;
    private SiteType siteType;
}
