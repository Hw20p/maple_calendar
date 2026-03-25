package com.maple.project.calendar.dto;

import lombok.Builder;
import lombok.Getter;
import com.maple.project.domain.boss.entity.BossRecord;
import com.maple.project.domain.hunting.entity.HuntingRecord;
import com.maple.project.domain.item.entity.ItemRecord;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DayRecord {

    private LocalDate date;
    private List<BossRecord> bossRecords;
    private List<HuntingRecord> huntingRecords;
    private List<ItemRecord> itemRecords;

    public boolean hasAnyRecord() {
        return !bossRecords.isEmpty() || !huntingRecords.isEmpty() || !itemRecords.isEmpty();
    }

    public int totalRecordCount() {
        return bossRecords.size() + huntingRecords.size() + itemRecords.size();
    }
}
