package com.maple.project.domain.boss.service;

import com.maple.project.domain.character.entity.MapleCharacter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maple.project.client.NexonApiClient;
import com.maple.project.client.dto.BossApiResponse;
import com.maple.project.domain.boss.entity.BossRecord;
import com.maple.project.domain.boss.repository.BossRecordRepository;
import com.maple.project.domain.character.repository.MapleCharacterRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BossRecordService {

    private final BossRecordRepository bossRecordRepository;
    private final MapleCharacterRepository characterRepository;
    private final NexonApiClient nexonApiClient;

    /** Nexon API에서 보스 결정 현황 가져와서 오늘 날짜로 저장 */
    @Transactional
    public void syncBossRecordFromApi(Long characterId) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        BossApiResponse response = nexonApiClient.getBossRecord(character.getOcid());
        if (response == null || response.getBossList() == null) return;

        LocalDate today = LocalDate.now();

        for (BossApiResponse.BossInfo info : response.getBossList()) {
            boolean exists = bossRecordRepository
                    .findByCharacterAndRecordDate(character, today)
                    .stream()
                    .anyMatch(r -> r.getBossName().equals(info.getBossName())
                            && r.getDifficulty().equals(info.getBossDifficulty()));

            if (!exists) {
                BossRecord record = BossRecord.builder()
                        .character(character)
                        .bossName(info.getBossName())
                        .difficulty(info.getBossDifficulty())
                        .recordDate(today)
                        .cleared(Boolean.TRUE.equals(info.getBossKillFlag()))
                        .build();
                bossRecordRepository.save(record);
            }
        }
    }

    /** 보스 기록 수동 추가 */
    @Transactional
    public BossRecord addRecord(Long characterId, String bossName, String difficulty,
                                LocalDate recordDate, boolean cleared, Long gainedMeso, String memo) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        BossRecord record = BossRecord.builder()
                .character(character)
                .bossName(bossName)
                .difficulty(difficulty)
                .recordDate(recordDate)
                .cleared(cleared)
                .gainedMeso(gainedMeso)
                .memo(memo)
                .build();
        return bossRecordRepository.save(record);
    }

    /** 보스 기록 수정 */
    @Transactional
    public void updateRecord(Long recordId, boolean cleared, Long gainedMeso, String memo) {
        BossRecord record = bossRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));
        record.update(cleared, gainedMeso, memo);
    }

    /** 보스 기록 삭제 */
    @Transactional
    public void deleteRecord(Long recordId) {
        bossRecordRepository.deleteById(recordId);
    }

    /** 월별 보스 기록 조회 */
    public List<BossRecord> findByMonth(Long characterId, int year, int month) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());
        return bossRecordRepository.findByCharacterAndRecordDateBetweenOrderByRecordDate(character, from, to);
    }

    /** 날짜별 보스 기록 조회 */
    public List<BossRecord> findByDate(Long characterId, LocalDate date) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        return bossRecordRepository.findByCharacterAndRecordDate(character, date);
    }
}
