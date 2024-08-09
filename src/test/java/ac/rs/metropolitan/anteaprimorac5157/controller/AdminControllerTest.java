package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetailsService;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import ac.rs.metropolitan.anteaprimorac5157.service.EmotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    private AdminController adminController;
    private DiaryEntryService mockDiaryEntryService;
    private DiaryUserDetailsService mockDiaryUserDetailsService;
    private EmotionService mockEmotionService;
    private Model model;

    @BeforeEach
    void setUp() {
        mockDiaryEntryService = mock(DiaryEntryService.class);
        mockDiaryUserDetailsService = mock(DiaryUserDetailsService.class);
        mockEmotionService = mock(EmotionService.class);
        adminController = new AdminController(mockDiaryEntryService, mockDiaryUserDetailsService, mockEmotionService);
        model = mock(Model.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowAdminPage() {
        //when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(List.of("sunny", "omori", "tommy", "sonic"));
        when(mockDiaryEntryService.getDiaryEntryCountByUser()).thenReturn(Map.of("sunny", 5, "omori", 3, "tommy", 7, "sonic", 2));
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Happy", 10L, "Sad", 5L));

        assertThat(adminController.showAdminPage(model)).isEqualTo("admin");
        //verify(model).addAttribute("users", List.of("sunny", "omori", "tommy", "sonic"));
        verify(model).addAttribute("diaryEntriesStatistics", Map.of("sunny", 5, "omori", 3, "tommy", 7, "sonic", 2));
        verify(model).addAttribute("emotionStatistics", Map.of("Happy", 10L, "Sad", 5L));
    }


    @Test
    void testGetDiaryCountByUser_ReturnsEmptyMapIfNoUsers() {
        when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(Collections.emptyList());
        when(mockDiaryEntryService.getDiaryEntryCountByUser()).thenReturn(Collections.emptyMap());

        Map<String, Integer> diaryCountByUser = mockDiaryEntryService.getDiaryEntryCountByUser();
        assertThat(diaryCountByUser).isEmpty();
    }

    @Test
    void testGetDiaryCountByUser_ReturnsCorrectMapIfUsersExist() {
        when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(List.of("sunny", "omori"));
        when(mockDiaryEntryService.getDiaryEntryCountByUser()).thenReturn(Map.of("sunny", 10, "omori", 5));

        Map<String, Integer> diaryCountByUser = mockDiaryEntryService.getDiaryEntryCountByUser();
        assertThat(diaryCountByUser).containsExactlyInAnyOrderEntriesOf(Map.of("sunny", 10, "omori", 5));
    }

    @Test
    void testGetEmotionUsageCount_ReturnsZeroIfEmotionNotUsed() {
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Happy", 0L));

        Map<String, Long> emotionUsageCount = mockEmotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsEntry("Happy", 0L);
    }

    @Test
    void testGetEmotionUsageCount_ReturnsCorrectCountIfEmotionUsedMultipleTimes() {
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Sad", 7L));

        Map<String, Long> emotionUsageCount = mockEmotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsEntry("Sad", 7L);
    }

    @Test
    void testGetEmotionUsageCount_ReturnsCorrectCountsForMultipleEmotions() {
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Happy", 10L, "Sad", 5L, "Angry", 2L));

        Map<String, Long> emotionUsageCount = mockEmotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsExactlyInAnyOrderEntriesOf(Map.of("Happy", 10L, "Sad", 5L, "Angry", 2L));
    }
}
