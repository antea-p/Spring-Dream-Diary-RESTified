package ac.rs.metropolitan.anteaprimorac5157.controller.api;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import ac.rs.metropolitan.anteaprimorac5157.security.JWTAuthentication;
import ac.rs.metropolitan.anteaprimorac5157.security.UserDetailsWithID;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/diary")
public class APIDiaryController {
    private final DiaryEntryService diaryEntryService;

    @Autowired
    public APIDiaryController(DiaryEntryService diaryEntryService) {
        this.diaryEntryService = diaryEntryService;
    }

    @GetMapping
    public List<DiaryEntry> list() {
        JWTAuthentication auth = (JWTAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return List.of();
        }
        UserDetailsWithID currentUser = (UserDetailsWithID) auth.getPrincipal();

        return diaryEntryService.list(currentUser.getId(), null, null, null);
    }

    @GetMapping("/{id}")
    public DiaryEntry getDiaryEntry(@PathVariable int id) {
        JWTAuthentication auth = (JWTAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        UserDetailsWithID currentUser = (UserDetailsWithID) auth.getPrincipal();

        return diaryEntryService.get(id, currentUser.getId()).orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Diary entry not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteDiaryEntry(@PathVariable int id) {
        JWTAuthentication auth = (JWTAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return;
        }
        UserDetailsWithID currentUser = (UserDetailsWithID) auth.getPrincipal();

        diaryEntryService.delete(id, currentUser.getId());
    }

    @PostMapping
    public DiaryEntry createDiaryEntry(@RequestBody DiaryEntry diaryEntry) {
        JWTAuthentication auth = (JWTAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        UserDetailsWithID currentUser = (UserDetailsWithID) auth.getPrincipal();

        diaryEntry.setCreatedDate(LocalDate.now());
        diaryEntry.setUserId(currentUser.getId());

        return diaryEntryService.save(diaryEntry);
    }

}
