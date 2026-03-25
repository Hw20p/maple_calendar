package com.maple.project.domain.character.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.maple.project.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "maple_character")
public class MapleCharacter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 넥슨 OCID (Open API 식별자) */
    @Column(nullable = false, unique = true, length = 200)
    private String ocid;

    /** 캐릭터명 */
    @Column(nullable = false, length = 50)
    private String characterName;

    /** 직업 */
    @Column(length = 50)
    private String characterClass;

    /** 레벨 */
    private Integer characterLevel;

    /** 월드명 */
    @Column(length = 30)
    private String worldName;

    /** 캐릭터 이미지 URL */
    @Column(columnDefinition = "TEXT")
    private String characterImage;

    /** 대표 캐릭터 여부 */
    @Column(nullable = false)
    private boolean mainCharacter = false;

    @Builder
    public MapleCharacter(String ocid, String characterName, String characterClass,
                          Integer characterLevel, String worldName, String characterImage) {
        this.ocid = ocid;
        this.characterName = characterName;
        this.characterClass = characterClass;
        this.characterLevel = characterLevel;
        this.worldName = worldName;
        this.characterImage = characterImage;
    }

    public void updateFromApi(String characterClass, Integer characterLevel,
                              String worldName, String characterImage) {
        this.characterClass = characterClass;
        this.characterLevel = characterLevel;
        this.worldName = worldName;
        this.characterImage = characterImage;
    }

    public void setMainCharacter(boolean mainCharacter) {
        this.mainCharacter = mainCharacter;
    }
}
