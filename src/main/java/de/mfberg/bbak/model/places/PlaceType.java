package de.mfberg.bbak.model.places;

import java.util.HashMap;
import java.util.Map;

public enum PlaceType {
    NONE,
    FOREST;

    private static final Map<Class<? extends PlaceBase>, PlaceType> placeTypeMap = new HashMap<>();

    static {
        placeTypeMap.put(Obstacle.class, FOREST);
        // Register other types as needed
    }

    public static PlaceType fromPlaceBase(PlaceBase place) {
        return placeTypeMap.getOrDefault(place.getClass(), NONE);
    }
}