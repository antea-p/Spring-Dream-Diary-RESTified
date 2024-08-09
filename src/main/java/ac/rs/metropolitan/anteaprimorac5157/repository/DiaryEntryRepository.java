package ac.rs.metropolitan.anteaprimorac5157.repository;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Integer> {
    List<DiaryEntry> findAll(Sort sort);

    @Query("SELECT COUNT(diary_entry) FROM DiaryEntry diary_entry WHERE diary_entry.userId = :userId")
    int countByUserId(@Param("userId") Integer userId);

    @Query("SELECT tag.name AS name, COUNT(tag.name) AS count FROM DiaryEntry diary_entry JOIN diary_entry.tags tag GROUP BY tag.name")
    List<TagUsage> countTagUsage();

    List<DiaryEntry> findByUserId(Integer userId, Sort sort);

    List<DiaryEntry> findByUserIdAndTitleContainingIgnoreCase(Integer userId, String title, Sort sort);

    Optional<DiaryEntry> findByIdAndUserId(Integer id, Integer userId);
}
