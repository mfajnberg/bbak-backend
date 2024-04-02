package de.mfberg.bbak.model.places;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class PlaceBase {
    @Id
    @GeneratedValue
    private long id;
    private boolean isBlocking = false;
}
