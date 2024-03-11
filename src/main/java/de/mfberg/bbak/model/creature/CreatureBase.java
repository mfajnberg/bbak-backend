package de.mfberg.bbak.model.creature;

import de.mfberg.bbak.model.item.ItemBase;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data
public class CreatureBase {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    private Integer intellect;
    private Integer discipline;
    private Integer power;
    private Integer agility;
    private Integer lucidity;
    private Integer charisma;

    private Integer alignment;
    private Integer temperament;
    private Integer morale;

    private Party party;

    private List<ItemBase> backpack;
}
