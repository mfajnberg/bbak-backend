package de.mfberg.bbak.dto;

import de.mfberg.bbak.model.worldmap.HexVector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TravelRequest {
    private List<HexVector> path;
}
