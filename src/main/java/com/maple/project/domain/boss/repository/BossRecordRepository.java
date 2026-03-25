package com.maple.project.domain.boss.repository;

import com.maple.project.domain.boss.entity.BossRecord;
import com.maple.project.domain.character.entity.MapleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BossRecordRepository extends JpaRepository<BossRecord, Long> {

    List<BossRecord> findByCharacterAndRecordDateBetweenOrderByRecordDate(
            MapleCharacter character, LocalDate from, LocalDate to);

    List<BossRecord> findByCharacterAndRecordDate(MapleCharacter character, LocalDate recordDate);

    List<BossRecord> findByRecordDateBetweenOrderByRecordDate(LocalDate from, LocalDate to);
}
