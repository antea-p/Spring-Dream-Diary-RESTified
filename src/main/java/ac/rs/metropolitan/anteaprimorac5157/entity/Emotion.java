package ac.rs.metropolitan.anteaprimorac5157.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "emotions", schema = "dream_diary")
public class Emotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    public Emotion() {
    }

    public Emotion(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Emotion setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Emotion setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emotion emotion = (Emotion) o;
        return Objects.equals(id, emotion.id) && Objects.equals(name, emotion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Emotion{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
