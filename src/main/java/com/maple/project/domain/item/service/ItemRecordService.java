package com.maple.project.domain.item.service;

import com.maple.project.domain.character.entity.MapleCharacter;
import com.maple.project.domain.character.repository.MapleCharacterRepository;
import com.maple.project.domain.item.entity.ItemRecord;
import com.maple.project.domain.item.repository.ItemRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRecordService {

    private final ItemRecordRepository itemRecordRepository;
    private final MapleCharacterRepository characterRepository;

    @Transactional
    public ItemRecord addRecord(Long characterId, LocalDate recordDate, String itemName,
                                String obtainSource, String obtainLocation, String itemGrade,
                                Boolean tradeable, String memo) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        ItemRecord record = ItemRecord.builder()
                .character(character)
                .recordDate(recordDate)
                .itemName(itemName)
                .obtainSource(obtainSource)
                .obtainLocation(obtainLocation)
                .itemGrade(itemGrade)
                .tradeable(tradeable)
                .memo(memo)
                .build();
        return itemRecordRepository.save(record);
    }

    @Transactional
    public void updateRecord(Long recordId, String itemName, String obtainSource,
                             String obtainLocation, String itemGrade, Boolean tradeable, String memo) {
        ItemRecord record = itemRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));
        record.update(itemName, obtainSource, obtainLocation, itemGrade, tradeable, memo);
    }

    @Transactional
    public void deleteRecord(Long recordId) {
        itemRecordRepository.deleteById(recordId);
    }

    public List<ItemRecord> findByMonth(Long characterId, int year, int month) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());
        return itemRecordRepository.findByCharacterAndRecordDateBetweenOrderByRecordDate(character, from, to);
    }

    public List<ItemRecord> findByDate(Long characterId, LocalDate date) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        return itemRecordRepository.findByCharacterAndRecordDate(character, date);
    }
}
