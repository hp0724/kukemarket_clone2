package kukekyakya.kukemarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// createAt 필드와 modifiedAt 엔티티에 자동 생성하기 위해서 어노테이션 추가
@EnableJpaAuditing
public class KukemarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(KukemarketApplication.class, args);
	}

}
