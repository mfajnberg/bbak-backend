package de.mfberg.bbak.model.items;

import de.mfberg.bbak.model.creatures.CreatureBase;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class ItemBase {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private CreatureBase owner;
}
