package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryEntryEmotionRepository;
import ac.rs.metropolitan.anteaprimorac5157.repository.EmotionRepository;
import ac.rs.metropolitan.anteaprimorac5157.repository.EmotionUsageCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmotionServiceTest {

    private EmotionRepository mockEmotionRepository;
    private DiaryEntryEmotionRepository mockDiaryEmotionRepository;
    private EmotionService emotionService;

    @BeforeEach
    void setUp() {
        this.mockEmotionRepository = mock(EmotionRepository.class);
        this.mockDiaryEmotionRepository = mock(DiaryEntryEmotionRepository.class);
        this.emotionService = new EmotionService(mockEmotionRepository, mockDiaryEmotionRepository);
    }

    @Test
    void testFindAllEmotions_ReturnsListOfEmotions() {
        List<Emotion> expectedResult = List.of(new Emotion(1, "Nostalgia"),
                                               new Emotion(2, "Wonder"));
        when(mockEmotionRepository.findAll()).thenReturn(expectedResult);

        List<Emotion> actualResult = emotionService.findAllEmotions();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void testGetEmotionUsageCount_ReturnsZeroIfEmotionNotUsed() {
        EmotionUsageCount mockUsageCount = mock(EmotionUsageCount.class);
        when(mockUsageCount.getEmotionName()).thenReturn("Happy");
        when(mockUsageCount.getCount()).thenReturn(0L);

        List<EmotionUsageCount> usageCounts = List.of(mockUsageCount);
        when(mockDiaryEmotionRepository.findEmotionUsageCount()).thenReturn(usageCounts);

        Map<String, Long> emotionUsageCount = emotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsEntry("Happy", 0L);
    }

    @Test
    void testGetEmotionUsageCount_ReturnsCorrectCountIfEmotionUsedMultipleTimes() {
        EmotionUsageCount mockUsageCount = mock(EmotionUsageCount.class);
        when(mockUsageCount.getEmotionName()).thenReturn("Sad");
        when(mockUsageCount.getCount()).thenReturn(7L);

        List<EmotionUsageCount> usageCounts = List.of(mockUsageCount);
        when(mockDiaryEmotionRepository.findEmotionUsageCount()).thenReturn(usageCounts);

        Map<String, Long> emotionUsageCount = emotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsEntry("Sad", 7L);
    }

    @Test
    void testGetEmotionUsageCount_ReturnsCorrectCountsForMultipleEmotions() {
        EmotionUsageCount mockUsageCount1 = mock(EmotionUsageCount.class);
        when(mockUsageCount1.getEmotionName()).thenReturn("Happy");
        when(mockUsageCount1.getCount()).thenReturn(10L);

        EmotionUsageCount mockUsageCount2 = mock(EmotionUsageCount.class);
        when(mockUsageCount2.getEmotionName()).thenReturn("Sad");
        when(mockUsageCount2.getCount()).thenReturn(5L);

        EmotionUsageCount mockUsageCount3 = mock(EmotionUsageCount.class);
        when(mockUsageCount3.getEmotionName()).thenReturn("Angry");
        when(mockUsageCount3.getCount()).thenReturn(2L);

        List<EmotionUsageCount> usageCounts = List.of(mockUsageCount1, mockUsageCount2, mockUsageCount3);
        when(mockDiaryEmotionRepository.findEmotionUsageCount()).thenReturn(usageCounts);

        Map<String, Long> emotionUsageCount = emotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsExactlyInAnyOrderEntriesOf(Map.of("Happy", 10L, "Sad", 5L, "Angry", 2L));
    }
}
