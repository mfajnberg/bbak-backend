package de.mfberg.bbak.model.adventuremap;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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
}
