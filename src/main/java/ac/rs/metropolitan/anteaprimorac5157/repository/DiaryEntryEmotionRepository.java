package ac.rs.metropolitan.anteaprimorac5157.repository;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntryEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryEntryEmotionRepository extends JpaRepository<DiaryEntryEmotion, Integer> {

    @Query("SELECT emotion.name AS emotionName, COUNT(diary_entry_emotion) AS count " +
            "FROM DiaryEntryEmotion diary_entry_emotion " +
            "JOIN diary_entry_emotion.emotion emotion " +
            "GROUP BY emotion.name")
    List<EmotionUsageCount> findEmotionUsageCount();

    @Query("SELECT diary_entry_emotion FROM DiaryEntryEmotion diary_entry_emotion WHERE diary_entry_emotion.entry.id = :entryId")
    List<DiaryEntryEmotion> findByEntryId(@Param("entryId") Integer entryId);

}