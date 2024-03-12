package de.mfberg.bbak.model.creature;

import de.mfberg.bbak.model.Party;
import de.mfberg.bbak.model.item.ItemBase;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class CreatureBase {
    @Id
    @GeneratedValue
    private Long id;

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

    @ManyToOne
    private Party party;
    @OneToMany(mappedBy = "owner")
    private List<ItemBase> backpack;
}
