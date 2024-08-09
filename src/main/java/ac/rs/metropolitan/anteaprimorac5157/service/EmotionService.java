package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryEntryEmotionRepository;
import ac.rs.metropolitan.anteaprimorac5157.repository.EmotionRepository;
import ac.rs.metropolitan.anteaprimorac5157.repository.EmotionUsageCount;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmotionService {

    private final EmotionRepository emotionRepository;
    private final DiaryEntryEmotionRepository diaryEntryEmotionRepository;

    public EmotionService(EmotionRepository emotionRepository, DiaryEntryEmotionRepository diaryEntryEmotionRepository) {
        this.emotionRepository = emotionRepository;
        this.diaryEntryEmotionRepository = diaryEntryEmotionRepository;
    }

    public List<Emotion> findAllEmotions() {
        return emotionRepository.findAll();
    }

    public Map<String, Long> getEmotionUsageCount() {
        List<EmotionUsageCount> usageCounts = diaryEntryEmotionRepository.findEmotionUsageCount();
        Map<String, Long> emotionUsageCount = new HashMap<>();

        for (EmotionUsageCount emotion : usageCounts) {
            emotionUsageCount.put(emotion.getEmotionName(), emotion.getCount());
        }

        return emotionUsageCount;
    }


}
