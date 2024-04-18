package de.mfberg.bbak.model.places;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Obstacle extends PlaceBase {
    private final boolean isBlocking = true;
    @Transient
    public static final String assetName = "Forest.glb";
}
