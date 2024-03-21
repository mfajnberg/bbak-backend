package de.mfberg.bbak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAvatarRequest {
    private CreatureDTO leader;
    private List<CreatureDTO> members;
}
