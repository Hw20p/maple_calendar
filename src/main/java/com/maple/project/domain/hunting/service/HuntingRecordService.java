package com.maple.project.domain.hunting.service;

import com.maple.project.domain.character.entity.MapleCharacter;
import com.maple.project.domain.hunting.repository.HuntingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maple.project.domain.character.repository.MapleCharacterRepository;
import com.maple.project.domain.hunting.entity.HuntingRecord;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HuntingRecordService {

    private final HuntingRecordRepository huntingRecordRepository;
    private final MapleCharacterRepository characterRepository;

    @Transactional
    public HuntingRecord addRecord(Long characterId, LocalDate recordDate, String huntingGround,
                                   Integer durationMinutes, Long gainedExp, Long gainedMeso, String memo) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        HuntingRecord record = HuntingRecord.builder()
                .character(character)
                .recordDate(recordDate)
                .huntingGround(huntingGround)
                .durationMinutes(durationMinutes)
                .gainedExp(gainedExp)
                .gainedMeso(gainedMeso)
                .memo(memo)
                .build();
        return huntingRecordRepository.save(record);
    }

    @Transactional
    public void updateRecord(Long recordId, String huntingGround, Integer durationMinutes,
                             Long gainedExp, Long gainedMeso, String memo) {
        HuntingRecord record = huntingRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));
        record.update(huntingGround, durationMinutes, gainedExp, gainedMeso, memo);
    }

    @Transactional
    public void deleteRecord(Long recordId) {
        huntingRecordRepository.deleteById(recordId);
    }

    public List<HuntingRecord> findByMonth(Long characterId, int year, int month) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());
        return huntingRecordRepository.findByCharacterAndRecordDateBetweenOrderByRecordDate(character, from, to);
    }

    public List<HuntingRecord> findByDate(Long characterId, LocalDate date) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        return huntingRecordRepository.findByCharacterAndRecordDate(character, date);
    }
}
