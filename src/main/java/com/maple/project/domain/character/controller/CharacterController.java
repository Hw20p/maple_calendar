package com.maple.project.domain.character.controller;

import com.maple.project.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("characters", characterService.findAll());
        return "character/list";
    }

    @PostMapping("/register")
    public String register(@RequestParam String characterName, RedirectAttributes redirectAttributes) {
        try {
            characterService.registerCharacter(characterName);
            redirectAttributes.addFlashAttribute("successMessage", characterName + " 캐릭터가 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "캐릭터 등록 실패: " + e.getMessage());
        }
        return "redirect:/characters";
    }

    @PostMapping("/{id}/main")
    public String setMain(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        characterService.setMainCharacter(id);
        redirectAttributes.addFlashAttribute("successMessage", "대표 캐릭터가 변경되었습니다.");
        return "redirect:/characters";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        characterService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "캐릭터가 삭제되었습니다.");
        return "redirect:/characters";
    }
}
