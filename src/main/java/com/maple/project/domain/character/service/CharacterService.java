package com.maple.project.domain.character.service;

import com.maple.project.domain.character.entity.MapleCharacter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maple.project.client.NexonApiClient;
import com.maple.project.client.dto.CharacterBasicResponse;
import com.maple.project.client.dto.CharacterOcidResponse;
import com.maple.project.domain.character.repository.MapleCharacterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {

    private final MapleCharacterRepository characterRepository;
    private final NexonApiClient nexonApiClient;

    /** 캐릭터명으로 Nexon API 조회 후 DB에 저장 */
    @Transactional
    public MapleCharacter registerCharacter(String characterName) {
        CharacterOcidResponse ocidResponse = nexonApiClient.getOcid(characterName);
        String ocid = ocidResponse.getOcid();

        // 이미 등록된 캐릭터면 정보 업데이트
        return characterRepository.findByOcid(ocid)
                .map(existing -> {
                    CharacterBasicResponse basic = nexonApiClient.getCharacterBasic(ocid);
                    existing.updateFromApi(basic.getCharacterClass(), basic.getCharacterLevel(),
                            basic.getWorldName(), basic.getCharacterImage());
                    return existing;
                })
                .orElseGet(() -> {
                    CharacterBasicResponse basic = nexonApiClient.getCharacterBasic(ocid);
                    MapleCharacter character = MapleCharacter.builder()
                            .ocid(ocid)
                            .characterName(basic.getCharacterName())
                            .characterClass(basic.getCharacterClass())
                            .characterLevel(basic.getCharacterLevel())
                            .worldName(basic.getWorldName())
                            .characterImage(basic.getCharacterImage())
                            .build();
                    return characterRepository.save(character);
                });
    }

    /** 대표 캐릭터 설정 */
    @Transactional
    public void setMainCharacter(Long characterId) {
        characterRepository.findByMainCharacterTrue()
                .ifPresent(c -> c.setMainCharacter(false));

        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        character.setMainCharacter(true);
    }

    /** 전체 캐릭터 목록 조회 */
    public List<MapleCharacter> findAll() {
        return characterRepository.findAllByOrderByMainCharacterDescCharacterLevelDesc();
    }

    /** 캐릭터 단건 조회 */
    public MapleCharacter findById(Long id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
    }

    /** 캐릭터 삭제 */
    @Transactional
    public void delete(Long characterId) {
        characterRepository.deleteById(characterId);
    }
}
