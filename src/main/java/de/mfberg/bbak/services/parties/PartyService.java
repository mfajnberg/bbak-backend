package de.mfberg.bbak.services.parties;

import de.mfberg.bbak.dto.CreatureDTO;
import de.mfberg.bbak.dto.PartyDTO;
import de.mfberg.bbak.dto.TravelRequest;
import de.mfberg.bbak.exceptions.JobConflictException;
import de.mfberg.bbak.exceptions.InvalidDataException;
import de.mfberg.bbak.model.creatures.Avatar;
import de.mfberg.bbak.model.creatures.CreatureBase;
import de.mfberg.bbak.model.parties.Party;
import de.mfberg.bbak.model.places.PlaceBase;
import de.mfberg.bbak.model.worldmap.HexTile;
import de.mfberg.bbak.model.worldmap.HexVector;
import de.mfberg.bbak.repo.CreatureRepository;
import de.mfberg.bbak.repo.HexRepository;
import de.mfberg.bbak.repo.PartyRepository;
import de.mfberg.bbak.services.common.QuartzService;
import de.mfberg.bbak.services.common.ExtractionService;
import de.mfberg.bbak.services.creatures.CreatureFactory;
import de.mfberg.bbak.jobs.TravelJob;
import de.mfberg.bbak.jobs.TravelJobInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final CreatureRepository creatureRepository;
    private final PartyRepository partyRepository;
    private final HexRepository hexRepository;
    private final ExtractionService extractionService;
    private final QuartzService quartzService;

    @Transactional
    public PartyDTO getParty(HttpServletRequest request) {
        Party party = extractionService.partyFromClaim(request);
        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setLeader(new CreatureDTO(party.getLeader()));
        List<CreatureBase> members;
        members = creatureRepository.findAllPartyMembers(party.getId());
        partyDTO.setMembers(new ArrayList<>());
        members.forEach(member -> {
            partyDTO.getMembers().add(new CreatureDTO(member));
        });

        try {
            for (JobKey jobKey : quartzService.getScheduler().getJobKeys(GroupMatcher.anyGroup())) {
                if (jobKey.getName().contains(party.getId() + "~")) {
                    Scheduler scheduler = quartzService.getScheduler();
                    Trigger trigger = scheduler.getTriggersOfJob(jobKey).getFirst();

                    Date date = trigger.getStartTime();
                    partyDTO.setRemainingJobDurationMillis(date.getTime() - System.currentTimeMillis());
                    partyDTO.setDestinationRelative(new HexVector(
                            party.getDestination().getAxial().getQ() - party.getLocation().getAxial().getQ(),
                            party.getDestination().getAxial().getR() - party.getLocation().getAxial().getR()
                    ));
                    break; // todo: inform of other job types as well
                }
            }
        } catch (Exception ignored) { }

        return partyDTO;
    }


    public void createParty(HttpServletRequest request, PartyDTO partyData) {
        Avatar leader = extractionService.avatarFromClaim(request);
        Party newParty = new Party();
        newParty.setLeader(leader);
        newParty.setLocation(hexRepository.getReferenceById(new HexVector(0, 0)));
        partyRepository.save(newParty);
        partyData.getMembers().forEach(memberDTO -> {
            CreatureBase newMember = null;
            newMember = new CreatureFactory().fromDTO(memberDTO);
            newMember.setParty(newParty);
            creatureRepository.save(newMember);
        });
    }


}
