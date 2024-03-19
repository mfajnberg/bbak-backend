package de.mfberg.bbak.model.creature;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class FaceData {
    @Id
    @GeneratedValue
    private Long id;

}
