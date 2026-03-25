package com.maple.project.domain.hunting.repository;

import com.maple.project.domain.character.entity.MapleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maple.project.domain.hunting.entity.HuntingRecord;

import java.time.LocalDate;
import java.util.List;

public interface HuntingRecordRepository extends JpaRepository<HuntingRecord, Long> {

    List<HuntingRecord> findByCharacterAndRecordDateBetweenOrderByRecordDate(
            MapleCharacter character, LocalDate from, LocalDate to);

    List<HuntingRecord> findByRecordDateBetweenOrderByRecordDate(LocalDate from, LocalDate to);

    List<HuntingRecord> findByCharacterAndRecordDate(MapleCharacter character, LocalDate recordDate);
}
