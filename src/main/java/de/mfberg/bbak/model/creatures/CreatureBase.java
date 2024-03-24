package de.mfberg.bbak.model.creatures;

import de.mfberg.bbak.model.party.Party;
import de.mfberg.bbak.model.items.ItemBase;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class CreatureBase {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
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

    @ManyToOne
    private Party party;
    @OneToMany(mappedBy = "owner")
    private List<ItemBase> backpack;
}
