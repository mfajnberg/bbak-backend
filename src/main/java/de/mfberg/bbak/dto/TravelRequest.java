package de.mfberg.bbak.dto;

import de.mfberg.bbak.model.adventuremap.HexTile;
import de.mfberg.bbak.model.adventuremap.HexVector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TravelRequest {
    private List<HexVector> path;
}
