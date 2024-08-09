package ac.rs.metropolitan.anteaprimorac5157.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "diary_entries", schema = "dream_diary")
public class DiaryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "entry_id")
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "diary_entry_emotions",
            joinColumns = @JoinColumn(name = "entry_id"),
            inverseJoinColumns = @JoinColumn(name = "emotion_id")
    )
    private List<Emotion> emotions = new ArrayList<>();

    public DiaryEntry() {
    }

    public DiaryEntry(String title, String content, LocalDate createdDate, Integer userId, List<Tag> tags, List<Emotion> emotions) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.userId = userId;
        this.tags = tags;
        this.emotions = emotions;
    }

    public DiaryEntry(Integer id, String title, String content, LocalDate createdDate, Integer userId, List<Tag> tags, List<Emotion> emotions) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.userId = userId;
        this.tags = tags;
        this.emotions = emotions;
    }

    public Integer getId() {
        return id;
    }

    public DiaryEntry setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DiaryEntry setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DiaryEntry setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public DiaryEntry setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public DiaryEntry setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public DiaryEntry setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public List<Emotion> getEmotions() {
        return emotions;
    }

    public DiaryEntry setEmotions(List<Emotion> emotions) {
        this.emotions = emotions;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaryEntry that = (DiaryEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(createdDate, that.createdDate) && Objects.equals(userId, that.userId) && Objects.equals(tags, that.tags) && Objects.equals(emotions, that.emotions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, createdDate, userId, tags, emotions);
    }

    @Override
    public String toString() {
        return "DiaryEntry{" +
                "id=" + id +
                ", title=" + title +
                ", content=" + content +
                ", createdDate=" + createdDate +
                ", userId=" + userId +
                ", tags=" + tags +
                ", emotions=" + emotions +
                '}';
    }
}
