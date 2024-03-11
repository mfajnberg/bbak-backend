package de.mfberg.bbak.services.admin;

import de.mfberg.bbak.dto.HexTileDTO;
import de.mfberg.bbak.repo.WorldRepository;
import de.mfberg.bbak.model.adventuremap.HexTile;
import de.mfberg.bbak.model.adventuremap.HexVector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WorldService {
    private final WorldRepository worldRepository;

    public List<HexTile> getHexTiles(Set<HexVector> ids) {
        return worldRepository.findAllById(ids);
    }
    public void updateWorldLayout(List<HexTileDTO> worldGenData) {

    }
}
