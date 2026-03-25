package com.maple.project.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CharacterBasicResponse {

    @JsonProperty("character_name")
    private String characterName;

    @JsonProperty("world_name")
    private String worldName;

    @JsonProperty("character_gender")
    private String characterGender;

    @JsonProperty("character_class")
    private String characterClass;

    @JsonProperty("character_level")
    private Integer characterLevel;

    @JsonProperty("character_exp")
    private Long characterExp;

    @JsonProperty("character_exp_rate")
    private String characterExpRate;

    @JsonProperty("character_guild_name")
    private String characterGuildName;

    @JsonProperty("character_image")
    private String characterImage;
}
