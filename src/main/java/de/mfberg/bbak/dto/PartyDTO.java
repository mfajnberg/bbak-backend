package de.mfberg.bbak.dto;

import de.mfberg.bbak.model.worldmap.HexVector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyDTO {
    private CreatureDTO leader;
    private List<CreatureDTO> members;
    private HexVector destinationRelative; // null means stationary
    private Long remainingJobDurationMillis;
}
