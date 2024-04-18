package de.mfberg.bbak.model.worldmap;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HexVector implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long q;
    private long r;

    public static long axialDistance(HexVector a, HexVector b) {
        return (Math.abs(a.q - b.q)
                + Math.abs(a.q + a.r - b.q - b.r)
                + Math.abs(a.r - b.r)) / 2;
    }

    public static Set<HexVector> makeGrid(long aroundAxialQ, long aroundAxialR, byte radius) {
        Set<HexVector> result = new HashSet<HexVector>();
        result.add(new HexVector(0, 0));
        for (int currentRing = 1; currentRing <= radius; currentRing++)
        {
            HexVector vectorNE = new HexVector(currentRing, -1 * currentRing);
            for (int i = 0; i < currentRing; i++)
                result.add(new HexVector(
                        vectorNE.getQ() + aroundAxialQ, i + vectorNE.getR() + aroundAxialR));

            HexVector vectorE = new HexVector(currentRing, 0);
            for (int i = 0; i < currentRing; i++)
                result.add(new HexVector(
                        -1 * i + vectorE.getQ() + aroundAxialQ, i + vectorE.getR() + aroundAxialR));

            HexVector vectorSE = new HexVector(0, currentRing);
            for (int i = 0; i < currentRing; i++)
                result.add(new HexVector(
                        -1 * i + vectorSE.getQ() + aroundAxialQ, vectorSE.getR() + aroundAxialR));

            HexVector vectorSW = new HexVector(-1 * currentRing, currentRing);
            for (int i = 0; i < currentRing; i++)
                result.add(new HexVector(
                        vectorSW.getQ() + aroundAxialQ, -1 * i + vectorSW.getR() + aroundAxialR));

            HexVector vectorW = new HexVector(-1 * currentRing, 0);
            for (int i = 0; i < currentRing; i++)
                result.add(new HexVector(
                        i + vectorW.getQ() + aroundAxialQ, -1 * i + vectorW.getR() + aroundAxialR));

            HexVector vectorNW = new HexVector(0, -1 * currentRing);
            for (int i = 0; i < currentRing; i++)
                result.add(new HexVector(
                        i + vectorNW.getQ() + aroundAxialQ, vectorNW.getR() + aroundAxialR));
        }
        return result;
    }
}
