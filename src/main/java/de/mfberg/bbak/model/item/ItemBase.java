package de.mfberg.bbak.model.item;

import de.mfberg.bbak.model.creature.CreatureBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class ItemBase {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private CreatureBase owner;
}
