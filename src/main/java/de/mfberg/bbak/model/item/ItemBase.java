package de.mfberg.bbak.model.item;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ItemBase {
    @Id
    @GeneratedValue
    private Long id;
}
