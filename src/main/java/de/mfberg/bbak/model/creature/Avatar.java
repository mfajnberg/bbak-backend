package de.mfberg.bbak.model.creature;

import de.mfberg.bbak.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Avatar extends CreatureBase {
    @OneToOne
    private User owner;
}
