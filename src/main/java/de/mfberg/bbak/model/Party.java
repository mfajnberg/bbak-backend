package de.mfberg.bbak.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Party {
    @Id
    @GeneratedValue
    private Long id;
}
