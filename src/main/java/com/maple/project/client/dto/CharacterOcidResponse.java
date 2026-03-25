package com.maple.project.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CharacterOcidResponse {

    @JsonProperty("ocid")
    private String ocid;
}
