package de.mfberg.bbak.jobs;

import de.mfberg.bbak.model.worldmap.HexVector;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelJobInfo implements Serializable {
     @Serial
     private static final long serialVersionUID = 1L;
     private final String groupLabel = "travelJobs";
     private String label;
     private Long partyId; // job identity (forbid >1 travel jobs for 1 party)
     private List<HexVector> path;
     private long durationMillis; // depends on the places on path[0]

     
}
