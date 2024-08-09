package ac.rs.metropolitan.anteaprimorac5157.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "diary_entry_emotions", schema = "dream_diary")
public class DiaryEntryEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_emotion_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "entry_id", nullable = false)
    private DiaryEntry entry;

    @ManyToOne
    @JoinColumn(name = "emotion_id", nullable = false)
    private Emotion emotion;

    public DiaryEntryEmotion() {
    }

    public DiaryEntryEmotion(Integer id, DiaryEntry entry, Emotion emotion) {
        this.id = id;
        this.entry = entry;
        this.emotion = emotion;
    }

    public Integer getId() {
        return id;
    }

    public DiaryEntryEmotion setId(Integer id) {
        this.id = id;
        return this;
    }

    public DiaryEntry getEntry() {
        return entry;
    }

    public DiaryEntryEmotion setEntry(DiaryEntry entry) {
        this.entry = entry;
        return this;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public DiaryEntryEmotion setEmotion(Emotion emotion) {
        this.emotion = emotion;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaryEntryEmotion that = (DiaryEntryEmotion) o;
        return Objects.equals(id, that.id) && Objects.equals(entry, that.entry) && Objects.equals(emotion, that.emotion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entry, emotion);
    }

    @Override
    public String toString() {
        return "DiaryEntryEmotion{" +
                "id=" + id +
                ", entry=" + entry +
                ", emotion=" + emotion +
                '}';
    }
}
