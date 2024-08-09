package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.entity.Tag;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntrySortingCriteria;
import ac.rs.metropolitan.anteaprimorac5157.service.EmotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DiaryControllerTest {

    public static final Integer DIARY_ENTRY_ID = 1;
    private DiaryEntryService mockDiaryEntryService;
    private EmotionService mockEmotionService;
    private DiaryController diaryController;
    private Model mockModel;

    private static final Integer USER_ID = 1;
    private static final DiaryUserDetails USER_DETAILS = new DiaryUserDetails(new DiaryUser(
            USER_ID, "omori", "password", false));
    private static final String TITLE = "Test title";
    private static final String CONTENT = "Test content";
    private static final List<Emotion> EMOTION_LIST = List.of(new Emotion(1, "Nostalgia"),
            new Emotion(2, "Wonder"), new Emotion(3, "Joy"));


    @BeforeEach
    void setUp() {
        this.mockDiaryEntryService = mock(DiaryEntryService.class);
        this.mockEmotionService = mock(EmotionService.class);
        this.diaryController = new DiaryController(mockDiaryEntryService, mockEmotionService);
        this.mockModel = mock(Model.class);

        when(mockEmotionService.findAllEmotions()).thenReturn(EMOTION_LIST);
    }

    private void prepareTagsParameter(Map<String, String> parameters, String tags) {
        if (tags != null) {
            parameters.put("tags", tags);
        }
    }

    @Test
    void testShowDiaryList_NoSorting() {
        DiaryEntry entry1 = new DiaryEntry(1, "Gamma", "Content 1", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Alpha", "Content 3", LocalDate.now(), USER_ID, List.of(), List.of());

        List<DiaryEntry> unsortedEntries = List.of(entry1, entry2, entry3);
        when(mockDiaryEntryService.list(USER_ID, Sort.Direction.ASC, DiaryEntrySortingCriteria.CREATEDDATE, null)).thenReturn(unsortedEntries);

        String viewName = diaryController.showDiaryList(null, null, null, USER_DETAILS, mockModel);
        verify(mockDiaryEntryService).list(USER_ID, Sort.Direction.ASC, DiaryEntrySortingCriteria.CREATEDDATE, null);
        assertThat(viewName).isEqualTo("list");
        verify(mockModel).addAttribute("diaryEntries", unsortedEntries);
    }

    @Test
    void testShowDiaryList_ByTitle_NoSorting() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), USER_ID, List.of(), List.of());

        List<DiaryEntry> expectedResult = List.of(entry3);
        String searchTitle = "amm";
        when(mockDiaryEntryService.list(USER_ID, Sort.Direction.ASC, DiaryEntrySortingCriteria.CREATEDDATE, searchTitle)).thenReturn(expectedResult);

        String viewName = diaryController.showDiaryList(null, null, searchTitle, USER_DETAILS, mockModel);
        verify(mockDiaryEntryService).list(USER_ID, Sort.Direction.ASC, DiaryEntrySortingCriteria.CREATEDDATE, searchTitle);
        assertThat(viewName).isEqualTo("list");
        verify(mockModel).addAttribute("diaryEntries", expectedResult);
    }

    @Test
    void testShowDiaryList_ByTitle_WithSorting() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), USER_ID, List.of(), List.of());

        List<DiaryEntry> sortedMatchedEntries = List.of(entry3);
        String searchTitle = "amm";
        when(mockDiaryEntryService.list(USER_ID, Sort.Direction.ASC, DiaryEntrySortingCriteria.TITLE, searchTitle)).thenReturn(sortedMatchedEntries);

        String viewName = diaryController.showDiaryList("title", "asc", searchTitle, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("list");
        verify(mockModel).addAttribute("diaryEntries", sortedMatchedEntries);
    }


    @Test
    void testShowDiaryList_SortedByTitle_Ascending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), USER_ID, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry1, entry2, entry3);
        when(mockDiaryEntryService.list(USER_ID, Sort.Direction.ASC, DiaryEntrySortingCriteria.TITLE, null)).thenReturn(sortedEntries);

        String viewName = diaryController.showDiaryList("title", "asc", null, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("list");
        verify(mockModel).addAttribute("diaryEntries", sortedEntries);
    }

    @Test
    void testShowDiaryList_SortedByTitle_Descending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Gamma", "Content 1", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), USER_ID, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Alpha", "Content 3", LocalDate.now(), USER_ID, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry3, entry2, entry1);
        when(mockDiaryEntryService.list(USER_ID, Sort.Direction.DESC, DiaryEntrySortingCriteria.TITLE, null)).thenReturn(sortedEntries);

        String viewName = diaryController.showDiaryList("title", "desc", null, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("list");
        verify(mockModel).addAttribute("diaryEntries", sortedEntries);
    }

    @Test
    void testShowDiaryList_SortedByDate_Ascending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Entry", "Content 1", LocalDate.now().minusDays(2), USER_ID, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Entry", "Content 2", LocalDate.now().minusDays(1), USER_ID, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Entry", "Content 3", LocalDate.now(), USER_ID, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry1, entry2, entry3);
        when(mockDiaryEntryService.list(USER_ID, Sort.Direction.ASC, DiaryEntrySortingCriteria.CREATEDDATE, null)).thenReturn(sortedEntries);

        String viewName = diaryController.showDiaryList("createdDate", "asc", null, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("list");
        verify(mockModel).addAttribute("diaryEntries", sortedEntries);
    }

    @Test
    void testShowDiaryList_SortedByDate_Descending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Entry", "Content 1", LocalDate.now().minusDays(2), USER_ID, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Entry", "Content 2", LocalDate.now().minusDays(1), USER_ID, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Entry", "Content 3", LocalDate.now(), USER_ID, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry3, entry2, entry1);
        when(mockDiaryEntryService.list(USER_ID, Sort.Direction.DESC, DiaryEntrySortingCriteria.CREATEDDATE, null)).thenReturn(sortedEntries);

        String viewName = diaryController.showDiaryList("createdDate", "desc", null, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("list");
        verify(mockModel).addAttribute("diaryEntries", sortedEntries);
    }

    @Test
    void testShowCreateForm() {
        assertThat(diaryController.showCreateForm(mockModel)).isEqualTo("create");

        verify(mockModel).addAttribute("diaryEntry", new DiaryEntry());
        verify(mockModel).addAttribute("emotions", EMOTION_LIST);

    }

    @Test
    void testSaveDiaryEntry_NoEmotions() {
        Map<String, String> PARAMETERS = new HashMap<>();

        PARAMETERS.put("title", TITLE);
        PARAMETERS.put("content", CONTENT);
        PARAMETERS.put("tags", "");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(PARAMETERS, USER_DETAILS, mockModel)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntry_OneEmotion() {
        Map<String, String> PARAMETERS = new HashMap<>();

        PARAMETERS.put("title", TITLE);
        PARAMETERS.put("content", CONTENT);
        PARAMETERS.put("tags", "");
        PARAMETERS.put("emotion_1", "1");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(), List.of(new Emotion(1, "Nostalgia")));
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(PARAMETERS, USER_DETAILS, mockModel)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntry_MultipleEmotions() {
        Map<String, String> PARAMETERS = new HashMap<>();

        PARAMETERS.put("title", TITLE);
        PARAMETERS.put("content", CONTENT);
        PARAMETERS.put("tags", "");

        for (Emotion emotion : EMOTION_LIST) {
            PARAMETERS.put("emotion_ " + emotion.getId(), emotion.getId().toString());
        }

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(), EMOTION_LIST);
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(PARAMETERS, USER_DETAILS, mockModel)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntry_NoTags() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        prepareTagsParameter(parameters, "");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(), List.of(), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS, mockModel)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntry_OneTag() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        prepareTagsParameter(parameters, "Library");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Library")), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS, mockModel)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntry_MultipleTags() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        prepareTagsParameter(parameters, "Library, Magic, Childhood");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Library"), new Tag("Magic"), new Tag("Childhood")), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS, mockModel)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntry_TitleExacly100Characters() {
        Map<String, String> parameters = new HashMap<>();
        String exactly100CharsTitle = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        parameters.put("title", exactly100CharsTitle);
        parameters.put("content", CONTENT);
        parameters.put("tags", "");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(exactly100CharsTitle, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS, mockModel)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }


    @Test
    void testSaveDiaryEntry_MissingTitle() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("title", "");
        parameters.put("content", CONTENT);
        parameters.put("tags", "");

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS, mockModel)).isEqualTo("create");
        verify(mockModel).addAttribute("titleError", "Title is required");
    }

    @Test
    void testSaveDiaryEntry_TitleOver100Characters() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("title", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        parameters.put("content", CONTENT);
        parameters.put("tags", "");

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS, mockModel)).isEqualTo("create");
        verify(mockModel).addAttribute("titleError", "Title is too long (max 100 characters)");
    }

    @Test
    void testSaveDiaryEntry_MissingContent() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("title", TITLE);
        parameters.put("content", "");
        parameters.put("tags", "");

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS, mockModel)).isEqualTo("create");
        verify(mockModel).addAttribute("contentError", "Content is required");
    }

    @Test
    @DisplayName("Test edit form populates correctly, including the previously saved tags and pre-selected emotions")
    void testShowEditForm() {
        DiaryEntry diaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag(1, "Library")), List.of(new Emotion(1, "Nostalgia")));

        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(diaryEntry));

        String viewName = diaryController.showEditForm(DIARY_ENTRY_ID, mockModel, USER_DETAILS);

        assertThat(viewName).isEqualTo("edit");
        verify(mockModel).addAttribute("diaryEntry", diaryEntry);
        verify(mockModel).addAttribute("allEmotions", EMOTION_LIST);
        verify(mockModel).addAttribute("selectedEmotionIds", List.of(1));
        verify(mockModel).addAttribute("commaSeparatedTags", "Library");
    }

    @Test
    @DisplayName("Test update success case where user changes title or content, but changes neither the tags list or selected emotions")
    void testUpdateDiaryEntry_NoTagOrEmotionChanges() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, "Old Title", "Old Content", LocalDate.now(), USER_DETAILS.getId(),
                List.of(), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTitle().equals(TITLE) &&
                        diaryEntry.getContent().equals(CONTENT) &&
                        diaryEntry.getTags().isEmpty() &&
                        diaryEntry.getEmotions().isEmpty()
        ));
    }


    @Test
    @DisplayName("Test update success case where user changes (adds, removes, or updates) one tag, but doesn't make changes to selected emotions")
    void testUpdateDiaryEntry_OneTagChanged_NoEmotionChanges() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Updated Tag");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Old Tag")), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 1 &&
                        diaryEntry.getTags().get(0).getName().equals("Updated Tag") &&
                        diaryEntry.getEmotions().isEmpty()
        ));
    }

    @Test
    @DisplayName("Test update success case where user changes two or more tags, but doesn't make changes to selected emotions")
    void testUpdateDiaryEntry_MultipleTagsChanged_NoEmotionChanges() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Tag1, Tag2");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Old Tag1"), new Tag("Old Tag2")), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 2 &&
                        diaryEntry.getTags().get(0).getName().equals("Tag1") &&
                        diaryEntry.getTags().get(1).getName().equals("Tag2") &&
                        diaryEntry.getEmotions().isEmpty()
        ));
    }

    @Test
    @DisplayName("Test update success case where user doesn't change any tag, but makes one change to list of selected emotions (selected or unselected an emotion)")
    void testUpdateDiaryEntry_NoTagChanges_OneEmotionChange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Tag1");
        parameters.put("emotion_1", "1");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Tag1")), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 1 &&
                        diaryEntry.getTags().get(0).getName().equals("Tag1") &&
                        diaryEntry.getEmotions().size() == 1 &&
                        diaryEntry.getEmotions().get(0).getId().equals(1)
        ));
    }


    @Test
    @DisplayName("Test update success case where user doesn't change any tag, but makes two or more changes to list of selected emotions")
    void testUpdateDiaryEntry_NoTagChanges_MultipleEmotionChange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Tag1");
        parameters.put("emotion_1", "1");
        parameters.put("emotion_2", "2");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Tag1")), List.of(new Emotion(3, "Joy")));
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 1 &&
                        diaryEntry.getTags().get(0).getName().equals("Tag1") &&
                        diaryEntry.getEmotions().size() == 2 &&
                        diaryEntry.getEmotions().stream().anyMatch(emotion -> emotion.getId().equals(1)) &&
                        diaryEntry.getEmotions().stream().anyMatch(emotion -> emotion.getId().equals(2))
        ));
    }


    @Test
    @DisplayName("Test update success case where user changes one tag, and makes one change to list of selected emotions")
    void testUpdateDiaryEntry_OneTagChange_OneEmotionChange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Updated Tag");
        parameters.put("emotion_1", "1");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Old Tag")), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 1 &&
                        diaryEntry.getTags().get(0).getName().equals("Updated Tag") &&
                        diaryEntry.getEmotions().size() == 1 &&
                        diaryEntry.getEmotions().get(0).getId().equals(1)
        ));
    }


    @Test
    @DisplayName("Test update success case where user changes one tag, and makes two or more changes to list of selected emotions")
    void testUpdateDiaryEntry_OneTagChanges_MultipleEmotionChange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Updated Tag");
        parameters.put("emotion_1", "1");
        parameters.put("emotion_2", "2");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Old Tag")), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 1 &&
                        diaryEntry.getTags().get(0).getName().equals("Updated Tag") &&
                        diaryEntry.getEmotions().size() == 2 &&
                        diaryEntry.getEmotions().stream().anyMatch(emotion -> emotion.getId().equals(1)) &&
                        diaryEntry.getEmotions().stream().anyMatch(emotion -> emotion.getId().equals(2))
        ));
    }


    @Test
    @DisplayName("Test update success case where user changes two or more tags, and makes one change to list of selected emotions")
    void testUpdateDiaryEntry_MultipleTagChanges_OneEmotionChange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Tag1, Tag2");
        parameters.put("emotion_1", "1");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Old Tag1"), new Tag("Old Tag2")), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 2 &&
                        diaryEntry.getTags().get(0).getName().equals("Tag1") &&
                        diaryEntry.getTags().get(1).getName().equals("Tag2") &&
                        diaryEntry.getEmotions().size() == 1 &&
                        diaryEntry.getEmotions().get(0).getId().equals(1)
        ));
    }


    @Test
    @DisplayName("Test update success case where user changes two or more tags, and makes two or more changes to list of selected emotions")
    void testUpdateDiaryEntry_MultipleTagChanges_MultipleEmotionChange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        parameters.put("tags", "Tag1, Tag2");
        parameters.put("emotion_1", "1");
        parameters.put("emotion_2", "2");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Old Tag1"), new Tag("Old Tag2")), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTags().size() == 2 &&
                        diaryEntry.getTags().get(0).getName().equals("Tag1") &&
                        diaryEntry.getTags().get(1).getName().equals("Tag2") &&
                        diaryEntry.getEmotions().size() == 2 &&
                        diaryEntry.getEmotions().stream().anyMatch(emotion -> emotion.getId().equals(1)) &&
                        diaryEntry.getEmotions().stream().anyMatch(emotion -> emotion.getId().equals(2))
        ));
    }


    @Test
    @DisplayName("Test update failure case where user submits the form with blank title")
    void testUpdateDiaryEntry_MissingTitle() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", ""); // Blank title
        parameters.put("content", CONTENT);
        parameters.put("tags", "");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(), List.of(), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("edit");
        verify(mockModel).addAttribute("titleError", "Title is required");
    }


    @Test
    @DisplayName("Test update success edge case where user submits the form with exactly 100 characters long title")
    void testUpdateDiaryEntry_TitleExactly100Characters() {
        Map<String, String> parameters = new HashMap<>();
        String exactly100CharsTitle = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        parameters.put("id", "1");
        parameters.put("title", exactly100CharsTitle);
        parameters.put("content", CONTENT);
        parameters.put("tags", "");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(), List.of(), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));
        when(mockDiaryEntryService.save(any(DiaryEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService).save(argThat(diaryEntry ->
                diaryEntry.getTitle().equals(exactly100CharsTitle) &&
                        diaryEntry.getContent().equals(CONTENT) &&
                        diaryEntry.getTags().isEmpty() &&
                        diaryEntry.getEmotions().isEmpty()
        ));
    }


    @Test
    @DisplayName("Test update failure case where user submits the form with title > 100 characters")
    void testUpdateDiaryEntry_TitleTooLong() {
        Map<String, String> parameters = new HashMap<>();
        String tooLongTitle = "a".repeat(101);

        parameters.put("id", "1");
        parameters.put("title", tooLongTitle);
        parameters.put("content", CONTENT);
        parameters.put("tags", "");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(), List.of(), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("edit");
        verify(mockModel).addAttribute("titleError", "Title is too long (max 100 characters)");
    }


    @Test
    @DisplayName("Test update failure case where user submits the form with blank content")
    void testUpdateDiaryEntry_MissingContent() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");
        parameters.put("title", TITLE);
        parameters.put("content", "");
        parameters.put("tags", "");

        DiaryEntry existingDiaryEntry = new DiaryEntry(DIARY_ENTRY_ID, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(), List.of(), List.of());
        when(mockDiaryEntryService.get(DIARY_ENTRY_ID, USER_ID)).thenReturn(Optional.of(existingDiaryEntry));

        String viewName = diaryController.updateDiaryEntry(parameters, USER_DETAILS, mockModel);

        assertThat(viewName).isEqualTo("edit");
        verify(mockModel).addAttribute("contentError", "Content is required");
    }

    @Test
    void testDeleteDiaryEntry() {
        Integer diaryEntryId = 1;
        doNothing().when(mockDiaryEntryService).delete(diaryEntryId, USER_ID);

        String viewName = diaryController.deleteDiaryEntry(diaryEntryId, USER_DETAILS);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).delete(diaryEntryId, USER_ID);
    }

    @Test
    void testShowDiaryEntry() {
        Integer diaryEntryId = 1;
        DiaryEntry diaryEntry = new DiaryEntry(diaryEntryId, TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag(1, "Library")), List.of(new Emotion(1, "Nostalgia")));

        when(mockDiaryEntryService.get(diaryEntryId, USER_ID)).thenReturn(Optional.of(diaryEntry));

        String viewName = diaryController.showDiaryEntry(diaryEntryId, mockModel, USER_DETAILS);

        assertThat(viewName).isEqualTo("show");
        verify(mockModel).addAttribute("diaryEntry", diaryEntry);
    }

    @Test
    void testShowDiaryEntry_NotFound() {
        Integer diaryEntryId = 1;

        when(mockDiaryEntryService.get(diaryEntryId, USER_ID)).thenReturn(Optional.empty());

        String viewName = diaryController.showDiaryEntry(diaryEntryId, mockModel, USER_DETAILS);

        assertThat(viewName).isEqualTo("redirect:/diary");
        verify(mockModel, never()).addAttribute(anyString(), any());
    }
}