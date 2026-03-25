package com.maple.project.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BossApiResponse {

    @JsonProperty("date")
    private String date;

    @JsonProperty("boss_list")
    private List<BossInfo> bossList;

    @Getter
    @NoArgsConstructor
    public static class BossInfo {

        @JsonProperty("boss_name")
        private String bossName;

        @JsonProperty("boss_level")
        private String bossLevel;

        @JsonProperty("boss_difficulty")
        private String bossDifficulty;

        @JsonProperty("boss_kill_flag")
        private Boolean bossKillFlag;

        @JsonProperty("boss_drop_item")
        private List<DropItem> bossDropItem;
    }

    @Getter
    @NoArgsConstructor
    public static class DropItem {

        @JsonProperty("item_name")
        private String itemName;
    }
}
