package ac.rs.metropolitan.anteaprimorac5157.security;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaryUserDetailsService implements UserDetailsService {

    private final DiaryUserRepository diaryUserRepository;

    public DiaryUserDetailsService(DiaryUserRepository diaryUserRepository) {
        this.diaryUserRepository = diaryUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DiaryUser diaryUser = diaryUserRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found!")
        );

        return new DiaryUserDetails(diaryUser);
    }

    public List<String> getAllUsernames() {
        return diaryUserRepository.findAll()
                .stream()
                .map(DiaryUser::getUsername)
                .collect(Collectors.toList());
    }
}
