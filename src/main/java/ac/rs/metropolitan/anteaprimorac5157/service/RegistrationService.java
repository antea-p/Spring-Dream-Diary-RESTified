package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.entity.RegistrationCommand;
import ac.rs.metropolitan.anteaprimorac5157.exception.RegistrationFailedException;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class RegistrationService {

    private DiaryUserRepository diaryUserRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(DiaryUserRepository diaryUserRepository, PasswordEncoder passwordEncoder) {
        this.diaryUserRepository = diaryUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails register(RegistrationCommand command) throws RegistrationFailedException {
        String username = command.getUsername();
        String password = command.getPassword();
        String repeatPassword = command.getRepeatPassword();
        boolean passwordsMatch = Objects.equals(password, repeatPassword);
        boolean userExists = diaryUserRepository.findByUsername(username).isPresent();

        if (!passwordsMatch) {
            throw new RegistrationFailedException("Passwords don't match!");
        }
        else if (userExists) {
            throw new RegistrationFailedException("User already exists!");
        }
        else {
            // ID će biti podešen kod umetanja u bazu podataka. save metoda vraća DiaryUser objekat sa dodijeljenim ID-jem.
            DiaryUser newUser = new DiaryUser(username, passwordEncoder.encode(password), false);
            DiaryUser savedUser = diaryUserRepository.save(newUser);
            return new DiaryUserDetails(savedUser);
        }

    }
}
