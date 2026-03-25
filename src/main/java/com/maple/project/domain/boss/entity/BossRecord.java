package com.maple.project.domain.boss.entity;

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
@Table(name = "boss_record",
        uniqueConstraints = @UniqueConstraint(columnNames = {"character_id", "boss_name", "difficulty", "record_date"}))
public class BossRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private MapleCharacter character;

    /** 보스명 (e.g. 카오스 벨룸) */
    @Column(nullable = false, length = 50)
    private String bossName;

    /** 난이도 (노말/하드/카오스 등) */
    @Column(length = 20)
    private String difficulty;

    /** 클리어 날짜 */
    @Column(nullable = false)
    private LocalDate recordDate;

    /** 클리어 여부 */
    @Column(nullable = false)
    private boolean cleared;

    /** 획득 메소 */
    private Long gainedMeso;

    /** 메모 */
    @Column(length = 500)
    private String memo;

    @Builder
    public BossRecord(MapleCharacter character, String bossName, String difficulty,
                      LocalDate recordDate, boolean cleared, Long gainedMeso, String memo) {
        this.character = character;
        this.bossName = bossName;
        this.difficulty = difficulty;
        this.recordDate = recordDate;
        this.cleared = cleared;
        this.gainedMeso = gainedMeso;
        this.memo = memo;
    }

    public void update(boolean cleared, Long gainedMeso, String memo) {
        this.cleared = cleared;
        this.gainedMeso = gainedMeso;
        this.memo = memo;
    }
}
