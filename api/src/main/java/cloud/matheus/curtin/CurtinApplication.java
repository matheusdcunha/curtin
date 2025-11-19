package cloud.matheus.curtin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CurtinApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurtinApplication.class, args);
	}

}
