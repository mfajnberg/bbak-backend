package de.mfberg.bbak.model.adventuremap;

import de.mfberg.bbak.model.site.SiteBase;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    private SiteBase site;
}
