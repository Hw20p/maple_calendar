package com.maple.project.domain.item.repository;

import com.maple.project.domain.character.entity.MapleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maple.project.domain.item.entity.ItemRecord;

import java.time.LocalDate;
import java.util.List;

public interface ItemRecordRepository extends JpaRepository<ItemRecord, Long> {

    List<ItemRecord> findByCharacterAndRecordDateBetweenOrderByRecordDate(
            MapleCharacter character, LocalDate from, LocalDate to);

    List<ItemRecord> findByRecordDateBetweenOrderByRecordDate(LocalDate from, LocalDate to);

    List<ItemRecord> findByCharacterAndRecordDate(MapleCharacter character, LocalDate recordDate);
}
