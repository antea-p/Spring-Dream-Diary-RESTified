package ac.rs.metropolitan.anteaprimorac5157;

import ac.rs.metropolitan.anteaprimorac5157.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class DreamDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamDiaryApplication.class, args);
	}

}
