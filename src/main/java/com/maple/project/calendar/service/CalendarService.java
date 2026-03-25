package com.maple.project.calendar.service;

import com.maple.project.calendar.dto.DayRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maple.project.domain.boss.entity.BossRecord;
import com.maple.project.domain.boss.repository.BossRecordRepository;
import com.maple.project.domain.character.entity.MapleCharacter;
import com.maple.project.domain.character.repository.MapleCharacterRepository;
import com.maple.project.domain.hunting.entity.HuntingRecord;
import com.maple.project.domain.hunting.repository.HuntingRecordRepository;
import com.maple.project.domain.item.entity.ItemRecord;
import com.maple.project.domain.item.repository.ItemRecordRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final MapleCharacterRepository characterRepository;
    private final BossRecordRepository bossRecordRepository;
    private final HuntingRecordRepository huntingRecordRepository;
    private final ItemRecordRepository itemRecordRepository;

    /**
     * 특정 캐릭터의 월별 캘린더 데이터 반환
     * 날짜 → DayRecord 맵으로 반환
     */
    public List<DayRecord> getMonthCalendar(Long characterId, int year, int month) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());

        List<BossRecord> bossRecords = bossRecordRepository
                .findByCharacterAndRecordDateBetweenOrderByRecordDate(character, from, to);
        List<HuntingRecord> huntingRecords = huntingRecordRepository
                .findByCharacterAndRecordDateBetweenOrderByRecordDate(character, from, to);
        List<ItemRecord> itemRecords = itemRecordRepository
                .findByCharacterAndRecordDateBetweenOrderByRecordDate(character, from, to);

        Map<LocalDate, List<BossRecord>> bossMap = bossRecords.stream()
                .collect(Collectors.groupingBy(BossRecord::getRecordDate));
        Map<LocalDate, List<HuntingRecord>> huntingMap = huntingRecords.stream()
                .collect(Collectors.groupingBy(HuntingRecord::getRecordDate));
        Map<LocalDate, List<ItemRecord>> itemMap = itemRecords.stream()
                .collect(Collectors.groupingBy(ItemRecord::getRecordDate));

        List<DayRecord> result = new ArrayList<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            result.add(DayRecord.builder()
                    .date(date)
                    .bossRecords(bossMap.getOrDefault(date, List.of()))
                    .huntingRecords(huntingMap.getOrDefault(date, List.of()))
                    .itemRecords(itemMap.getOrDefault(date, List.of()))
                    .build());
        }
        return result;
    }

    /** 특정 날짜의 상세 기록 조회 */
    public DayRecord getDayRecord(Long characterId, LocalDate date) {
        MapleCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        return DayRecord.builder()
                .date(date)
                .bossRecords(bossRecordRepository.findByCharacterAndRecordDate(character, date))
                .huntingRecords(huntingRecordRepository.findByCharacterAndRecordDate(character, date))
                .itemRecords(itemRecordRepository.findByCharacterAndRecordDate(character, date))
                .build();
    }
}
