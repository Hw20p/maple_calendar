package com.maple.project.calendar.controller;

import com.maple.project.calendar.dto.DayRecord;
import com.maple.project.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.maple.project.domain.boss.service.BossRecordService;
import com.maple.project.domain.character.entity.MapleCharacter;
import com.maple.project.domain.character.service.CharacterService;
import com.maple.project.domain.hunting.service.HuntingRecordService;
import com.maple.project.domain.item.service.ItemRecordService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final CharacterService characterService;
    private final BossRecordService bossRecordService;
    private final HuntingRecordService huntingRecordService;
    private final ItemRecordService itemRecordService;

    /** 루트 → 캘린더 메인으로 리다이렉트 */
    @GetMapping("/")
    public String root() {
        return "redirect:/calendar";
    }

    /** 캘린더 메인 */
    @GetMapping("/calendar")
    public String calendar(@RequestParam(required = false) Long characterId,
                           @RequestParam(required = false) Integer year,
                           @RequestParam(required = false) Integer month,
                           Model model) {
        List<MapleCharacter> characters = characterService.findAll();
        model.addAttribute("characters", characters);

        if (characters.isEmpty()) {
            return "calendar/main";
        }

        // 캐릭터 선택 (파라미터 없으면 대표 캐릭터 or 첫 번째)
        MapleCharacter selectedCharacter;
        if (characterId != null) {
            selectedCharacter = characterService.findById(characterId);
        } else {
            selectedCharacter = characters.stream()
                    .filter(MapleCharacter::isMainCharacter)
                    .findFirst()
                    .orElse(characters.get(0));
        }

        // 연/월 기본값: 현재
        YearMonth ym = (year != null && month != null)
                ? YearMonth.of(year, month)
                : YearMonth.now();

        List<DayRecord> dayRecords = calendarService.getMonthCalendar(
                selectedCharacter.getId(), ym.getYear(), ym.getMonthValue());

        model.addAttribute("selectedCharacter", selectedCharacter);
        model.addAttribute("yearMonth", ym);
        model.addAttribute("prevMonth", ym.minusMonths(1));
        model.addAttribute("nextMonth", ym.plusMonths(1));
        model.addAttribute("dayRecords", dayRecords);
        model.addAttribute("firstDayOfWeek", ym.atDay(1).getDayOfWeek().getValue() % 7); // 0=일요일

        return "calendar/main";
    }

    /** 날짜 상세 기록 */
    @GetMapping("/calendar/day")
    public String dayDetail(@RequestParam Long characterId,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            Model model) {
        MapleCharacter character = characterService.findById(characterId);
        DayRecord dayRecord = calendarService.getDayRecord(characterId, date);

        model.addAttribute("character", character);
        model.addAttribute("dayRecord", dayRecord);
        model.addAttribute("characters", characterService.findAll());
        return "calendar/day";
    }

    /** 보스 기록 추가 */
    @PostMapping("/calendar/boss/add")
    public String addBossRecord(@RequestParam Long characterId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
                                @RequestParam String bossName,
                                @RequestParam String difficulty,
                                @RequestParam(defaultValue = "false") boolean cleared,
                                @RequestParam(required = false) Long gainedMeso,
                                @RequestParam(required = false) String memo,
                                RedirectAttributes redirectAttributes) {
        bossRecordService.addRecord(characterId, bossName, difficulty, recordDate, cleared, gainedMeso, memo);
        redirectAttributes.addFlashAttribute("successMessage", "보스 기록이 추가되었습니다.");
        return "redirect:/calendar/day?characterId=" + characterId + "&date=" + recordDate;
    }

    /** 보스 기록 삭제 */
    @PostMapping("/calendar/boss/{id}/delete")
    public String deleteBossRecord(@PathVariable Long id,
                                   @RequestParam Long characterId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   RedirectAttributes redirectAttributes) {
        bossRecordService.deleteRecord(id);
        redirectAttributes.addFlashAttribute("successMessage", "보스 기록이 삭제되었습니다.");
        return "redirect:/calendar/day?characterId=" + characterId + "&date=" + date;
    }

    /** Nexon API 보스 결정 동기화 */
    @PostMapping("/calendar/boss/sync")
    public String syncBossRecord(@RequestParam Long characterId,
                                 @RequestParam(required = false) Integer year,
                                 @RequestParam(required = false) Integer month,
                                 RedirectAttributes redirectAttributes) {
        try {
            bossRecordService.syncBossRecordFromApi(characterId);
            redirectAttributes.addFlashAttribute("successMessage", "보스 결정 현황을 동기화했습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "동기화 실패: " + e.getMessage());
        }
        String redirect = "/calendar?characterId=" + characterId;
        if (year != null && month != null) redirect += "&year=" + year + "&month=" + month;
        return "redirect:" + redirect;
    }

    /** 사냥 기록 추가 */
    @PostMapping("/calendar/hunting/add")
    public String addHuntingRecord(@RequestParam Long characterId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
                                   @RequestParam String huntingGround,
                                   @RequestParam(required = false) Integer durationMinutes,
                                   @RequestParam(required = false) Long gainedExp,
                                   @RequestParam(required = false) Long gainedMeso,
                                   @RequestParam(required = false) String memo,
                                   RedirectAttributes redirectAttributes) {
        huntingRecordService.addRecord(characterId, recordDate, huntingGround, durationMinutes, gainedExp, gainedMeso, memo);
        redirectAttributes.addFlashAttribute("successMessage", "사냥 기록이 추가되었습니다.");
        return "redirect:/calendar/day?characterId=" + characterId + "&date=" + recordDate;
    }

    /** 사냥 기록 삭제 */
    @PostMapping("/calendar/hunting/{id}/delete")
    public String deleteHuntingRecord(@PathVariable Long id,
                                      @RequestParam Long characterId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      RedirectAttributes redirectAttributes) {
        huntingRecordService.deleteRecord(id);
        redirectAttributes.addFlashAttribute("successMessage", "사냥 기록이 삭제되었습니다.");
        return "redirect:/calendar/day?characterId=" + characterId + "&date=" + date;
    }

    /** 득템 기록 추가 */
    @PostMapping("/calendar/item/add")
    public String addItemRecord(@RequestParam Long characterId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
                                @RequestParam String itemName,
                                @RequestParam(required = false) String obtainSource,
                                @RequestParam(required = false) String obtainLocation,
                                @RequestParam(required = false) String itemGrade,
                                @RequestParam(required = false) Boolean tradeable,
                                @RequestParam(required = false) String memo,
                                RedirectAttributes redirectAttributes) {
        itemRecordService.addRecord(characterId, recordDate, itemName, obtainSource, obtainLocation, itemGrade, tradeable, memo);
        redirectAttributes.addFlashAttribute("successMessage", "득템 기록이 추가되었습니다.");
        return "redirect:/calendar/day?characterId=" + characterId + "&date=" + recordDate;
    }

    /** 득템 기록 삭제 */
    @PostMapping("/calendar/item/{id}/delete")
    public String deleteItemRecord(@PathVariable Long id,
                                   @RequestParam Long characterId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   RedirectAttributes redirectAttributes) {
        itemRecordService.deleteRecord(id);
        redirectAttributes.addFlashAttribute("successMessage", "득템 기록이 삭제되었습니다.");
        return "redirect:/calendar/day?characterId=" + characterId + "&date=" + date;
    }
}
