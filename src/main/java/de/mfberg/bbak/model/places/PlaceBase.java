package de.mfberg.bbak.model.places;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class PlaceBase {
    @Id
    @GeneratedValue
    private long id;
    private boolean isBlocking = false;
}
