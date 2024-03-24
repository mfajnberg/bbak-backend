package de.mfberg.bbak.dto;

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
}
