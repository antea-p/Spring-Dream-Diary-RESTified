package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetailsService;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import ac.rs.metropolitan.anteaprimorac5157.service.EmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final DiaryEntryService diaryEntryService;
    private final DiaryUserDetailsService diaryUserDetailsService;
    private final EmotionService emotionService;

    public AdminController(DiaryEntryService diaryEntryService, DiaryUserDetailsService diaryUserDetailsService, EmotionService emotionService) {
        this.diaryEntryService = diaryEntryService;
        this.diaryUserDetailsService = diaryUserDetailsService;
        this.emotionService = emotionService;
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("diaryEntriesStatistics", diaryEntryService.getDiaryEntryCountByUser());
        model.addAttribute("emotionStatistics", emotionService.getEmotionUsageCount());
        model.addAttribute("tagUsageStatistics", diaryEntryService.getTagUsageStatistics());
        return "admin";
    }

}
