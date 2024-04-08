package de.mfberg.bbak.services.creatures;

import de.mfberg.bbak.dto.CreatureDTO;
import de.mfberg.bbak.exceptions.InvalidDataException;
import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.creatures.CreatureBase;
import de.mfberg.bbak.model.creatures.CreatureType;
import de.mfberg.bbak.model.creatures.Pawn;

public class CreatureFactory {
    public CreatureBase fromDTO(CreatureDTO creatureData) {
        CreatureBase newCreature;
        switch (creatureData.getCreatureType()) {
            case CreatureType.AVATAR -> newCreature = new Avatar();
            case CreatureType.PAWN -> newCreature = new Pawn();
            default -> throw new InvalidDataException("Creature type unknown to creature factory.");
        }
        newCreature.setCreatureType(creatureData.getCreatureType());
        newCreature.setName(creatureData.getName());
        newCreature.setDescription(creatureData.getDescription());
        newCreature.setIntellect(creatureData.getIntellect());
        newCreature.setDiscipline(creatureData.getDiscipline());
        newCreature.setPower(creatureData.getPower());
        newCreature.setAgility(creatureData.getAgility());
        newCreature.setLucidity(creatureData.getLucidity());
        newCreature.setCharisma(creatureData.getCharisma());
        newCreature.setAlignment(creatureData.getAlignment());
        newCreature.setTemperament(creatureData.getTemperament());
        newCreature.setMorale(creatureData.getMorale());
        return newCreature;
    }
}
