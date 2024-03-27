package de.mfberg.bbak.jobs;

import de.mfberg.bbak.model.adventuremap.HexVector;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelData implements Serializable {
     @Serial
     private static final long serialVersionUID = 1L;
     private List<HexVector> path;
}
