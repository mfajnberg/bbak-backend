package de.mfberg.bbak.services.places;

import de.mfberg.bbak.model.places.Obstacle;
import de.mfberg.bbak.model.places.PlaceBase;
import de.mfberg.bbak.model.places.PlaceType;

public class PlaceFactory {
    public PlaceBase fromPlaceType(PlaceType placeType) {
        return switch (placeType) {
            case null -> null;
            case NONE -> null;
            case FOREST -> new Obstacle();
        };
    }
}
