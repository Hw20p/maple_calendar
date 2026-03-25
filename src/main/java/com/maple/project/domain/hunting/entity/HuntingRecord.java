package com.maple.project.domain.hunting.entity;

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
@Table(name = "hunting_record")
public class HuntingRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private MapleCharacter character;

    /** 사냥 날짜 */
    @Column(nullable = false)
    private LocalDate recordDate;

    /** 사냥터 (e.g. 레헬른, 아르카나) */
    @Column(nullable = false, length = 100)
    private String huntingGround;

    /** 사냥 시간 (분) */
    private Integer durationMinutes;

    /** 획득 경험치 */
    private Long gainedExp;

    /** 획득 메소 */
    private Long gainedMeso;

    /** 메모 */
    @Column(length = 500)
    private String memo;

    @Builder
    public HuntingRecord(MapleCharacter character, LocalDate recordDate, String huntingGround,
                         Integer durationMinutes, Long gainedExp, Long gainedMeso, String memo) {
        this.character = character;
        this.recordDate = recordDate;
        this.huntingGround = huntingGround;
        this.durationMinutes = durationMinutes;
        this.gainedExp = gainedExp;
        this.gainedMeso = gainedMeso;
        this.memo = memo;
    }

    public void update(String huntingGround, Integer durationMinutes,
                       Long gainedExp, Long gainedMeso, String memo) {
        this.huntingGround = huntingGround;
        this.durationMinutes = durationMinutes;
        this.gainedExp = gainedExp;
        this.gainedMeso = gainedMeso;
        this.memo = memo;
    }
}
