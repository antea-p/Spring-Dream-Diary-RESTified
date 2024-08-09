package ac.rs.metropolitan.anteaprimorac5157.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users", schema = "dream_diary")
public class DiaryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 150)
    private String password;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    public DiaryUser() {
    }

    public DiaryUser(String username, String password, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public DiaryUser(Integer id, String username, String password, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaryUser diaryUser = (DiaryUser) o;
        return Objects.equals(id, diaryUser.id) && Objects.equals(username, diaryUser.username) &&
                Objects.equals(password, diaryUser.password) && Objects.equals(isAdmin, diaryUser.isAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin);
    }
}
