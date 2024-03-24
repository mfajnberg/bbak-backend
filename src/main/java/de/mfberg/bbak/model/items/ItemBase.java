package de.mfberg.bbak.model.items;

import de.mfberg.bbak.model.creatures.CreatureBase;
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
