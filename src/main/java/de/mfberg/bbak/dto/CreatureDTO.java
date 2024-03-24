package de.mfberg.bbak.dto;

import de.mfberg.bbak.model.creatures.CreatureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatureDTO {
    private CreatureType creatureType;
    private String name;
    private String description;

    private Long intellect;
    private Long discipline;
    private Long power;
    private Long agility;
    private Long lucidity;
    private Long charisma;

    private Long alignment;
    private Long temperament;
    private Long morale;
}
