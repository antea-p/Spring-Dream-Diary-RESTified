package ac.rs.metropolitan.anteaprimorac5157.repository;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiaryUserRepository extends JpaRepository<DiaryUser, Integer> {
    Optional<DiaryUser> findByUsername(String username);
}
