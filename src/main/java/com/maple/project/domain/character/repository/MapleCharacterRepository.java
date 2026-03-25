package com.maple.project.domain.character.repository;

import com.maple.project.domain.character.entity.MapleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MapleCharacterRepository extends JpaRepository<MapleCharacter, Long> {

    Optional<MapleCharacter> findByOcid(String ocid);

    Optional<MapleCharacter> findByCharacterName(String characterName);

    List<MapleCharacter> findAllByOrderByMainCharacterDescCharacterLevelDesc();

    Optional<MapleCharacter> findByMainCharacterTrue();
}
