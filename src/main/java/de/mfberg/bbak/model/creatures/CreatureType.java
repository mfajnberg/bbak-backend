package de.mfberg.bbak.model.creatures;

import de.mfberg.bbak.model.places.PlaceBase;

import java.util.HashMap;
import java.util.Map;

public enum CreatureType {
    AVATAR,
    PAWN;

    private static final Map<Class<? extends CreatureBase>, CreatureType> creatureTypeMap = new HashMap<>();

    static {
        creatureTypeMap.put(Avatar.class, AVATAR);
        creatureTypeMap.put(Pawn.class, PAWN);
        // Register other types as needed
    }

    public static CreatureType fromCreatureBase(PlaceBase place) {
        return creatureTypeMap.get(place.getClass());
    }
}
