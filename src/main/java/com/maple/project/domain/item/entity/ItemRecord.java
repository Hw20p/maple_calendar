package com.maple.project.domain.item.entity;

import com.maple.project.domain.character.entity.MapleCharacter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.maple.project.global.entity.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_record")
public class ItemRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private MapleCharacter character;

    /** 득템 날짜 */
    @Column(nullable = false)
    private LocalDate recordDate;

    /** 아이템명 */
    @Column(nullable = false, length = 100)
    private String itemName;

    /** 획득 경로 (보스드랍/사냥/거래소 등) */
    @Column(length = 50)
    private String obtainSource;

    /** 획득 장소 (보스명 or 사냥터명) */
    @Column(length = 100)
    private String obtainLocation;

    /** 아이템 등급 (유니크/레전드리 등) */
    @Column(length = 20)
    private String itemGrade;

    /** 거래 가능 여부 */
    private Boolean tradeable;

    /** 메모 */
    @Column(length = 500)
    private String memo;

    @Builder
    public ItemRecord(MapleCharacter character, LocalDate recordDate, String itemName,
                      String obtainSource, String obtainLocation, String itemGrade,
                      Boolean tradeable, String memo) {
        this.character = character;
        this.recordDate = recordDate;
        this.itemName = itemName;
        this.obtainSource = obtainSource;
        this.obtainLocation = obtainLocation;
        this.itemGrade = itemGrade;
        this.tradeable = tradeable;
        this.memo = memo;
    }

    public void update(String itemName, String obtainSource, String obtainLocation,
                       String itemGrade, Boolean tradeable, String memo) {
        this.itemName = itemName;
        this.obtainSource = obtainSource;
        this.obtainLocation = obtainLocation;
        this.itemGrade = itemGrade;
        this.tradeable = tradeable;
        this.memo = memo;
    }
}
