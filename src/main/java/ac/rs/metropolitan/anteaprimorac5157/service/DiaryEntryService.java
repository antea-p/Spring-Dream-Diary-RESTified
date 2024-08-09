package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntryEmotion;
import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiaryEntryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final DiaryUserRepository diaryUserRepository;
    private final DiaryEntryEmotionRepository diaryEntryEmotionRepository;
    private final EmotionRepository emotionRepository;

    public DiaryEntryService(DiaryEntryRepository diaryEntryRepository, DiaryUserRepository diaryUserRepository,
                             DiaryEntryEmotionRepository diaryEntryEmotionRepository, EmotionRepository emotionRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.diaryUserRepository = diaryUserRepository;
        this.diaryEntryEmotionRepository = diaryEntryEmotionRepository;
        this.emotionRepository = emotionRepository;
    }

    public List<DiaryEntry> list(Integer userId, @Nullable Sort.Direction direction, @Nullable DiaryEntrySortingCriteria sortBy,
                                 @Nullable String title) {
        Sort sort = Sort.by(direction == null ? Sort.Direction.ASC : direction,
                sortBy == null ? "id" : sortBy.toString());
        return (title == null)?
                diaryEntryRepository.findByUserId(userId, sort)
                : diaryEntryRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title, sort);
    }

    public Optional<DiaryEntry> get(Integer id, Integer userId) {
        return diaryEntryRepository.findByIdAndUserId(id, userId).map(diaryEntry -> {
            List<Emotion> emotions = getEmotionsForDiaryEntry(diaryEntry.getId());
            diaryEntry.setEmotions(emotions);
            return diaryEntry;
        });
    }

    public DiaryEntry save(DiaryEntry diaryEntry) {
        return diaryEntryRepository.save(diaryEntry);
    }

    public void delete(Integer id, Integer userId) {
        Optional<DiaryEntry> entry = diaryEntryRepository.findByIdAndUserId(id, userId);
        entry.ifPresent(diaryEntryRepository::delete);
    }

    public void updateDiaryEntryEmotions(DiaryEntry diaryEntry, List<Emotion> selectedEmotions) {
        List<DiaryEntryEmotion> currentEmotions = diaryEntryEmotionRepository.findByEntryId(diaryEntry.getId());

        for (DiaryEntryEmotion currentEmotion : currentEmotions) {
            if (!selectedEmotions.contains(currentEmotion.getEmotion())) {
                diaryEntryEmotionRepository.delete(currentEmotion);
            }
        }

        for (Emotion selectedEmotion : selectedEmotions) {
            if (currentEmotions.stream().noneMatch(e -> e.getEmotion().equals(selectedEmotion))) {
                DiaryEntryEmotion newEntryEmotion = new DiaryEntryEmotion();
                newEntryEmotion.setEntry(diaryEntry);
                newEntryEmotion.setEmotion(selectedEmotion);
                diaryEntryEmotionRepository.save(newEntryEmotion);
            }
        }
    }

    public List<Emotion> getEmotionsForDiaryEntry(Integer diaryEntryId) {
        List<DiaryEntryEmotion> diaryEntryEmotions = diaryEntryEmotionRepository.findByEntryId(diaryEntryId);
        return diaryEntryEmotions.stream()
                .map(DiaryEntryEmotion::getEmotion)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getDiaryEntryCountByUser() {
        List<DiaryUser> users = diaryUserRepository.findAll();
        Map<String, Integer> diaryCountByUser = new HashMap<>();

        for (DiaryUser user : users) {
            int count = diaryEntryRepository.countByUserId(user.getId());
            diaryCountByUser.put(user.getUsername(), count);
        }

        return diaryCountByUser;
    }

    public List<TagUsage> getTagUsageStatistics() {
        return diaryEntryRepository.countTagUsage();
    }
}