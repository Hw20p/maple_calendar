package com.maple.project.client;

import com.maple.project.client.dto.CharacterBasicResponse;
import com.maple.project.client.dto.CharacterOcidResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.maple.project.client.dto.BossApiResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class NexonApiClient {

    private final WebClient nexonWebClient;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 캐릭터명으로 OCID 조회 */
    public CharacterOcidResponse getOcid(String characterName) {
        return nexonWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maplestory/v1/id")
                        .queryParam("character_name", characterName)
                        .build())
                .retrieve()
                .bodyToMono(CharacterOcidResponse.class)
                .block();
    }

    /** OCID로 캐릭터 기본 정보 조회 */
    public CharacterBasicResponse getCharacterBasic(String ocid) {
        return nexonWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maplestory/v1/character/basic")
                        .queryParam("ocid", ocid)
                        .build())
                .retrieve()
                .bodyToMono(CharacterBasicResponse.class)
                .block();
    }

    /** OCID로 특정 날짜의 캐릭터 기본 정보 조회 */
    public CharacterBasicResponse getCharacterBasic(String ocid, LocalDate date) {
        return nexonWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maplestory/v1/character/basic")
                        .queryParam("ocid", ocid)
                        .queryParam("date", date.format(DATE_FORMATTER))
                        .build())
                .retrieve()
                .bodyToMono(CharacterBasicResponse.class)
                .block();
    }

    /** OCID로 주간 보스 결정 현황 조회 */
    public BossApiResponse getBossRecord(String ocid) {
        return nexonWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maplestory/v1/character/boss")
                        .queryParam("ocid", ocid)
                        .build())
                .retrieve()
                .bodyToMono(BossApiResponse.class)
                .block();
    }
}
