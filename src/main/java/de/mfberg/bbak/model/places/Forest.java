package de.mfberg.bbak.model.places;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Forest extends PlaceBase {
    private boolean isBlocking = true;
}
