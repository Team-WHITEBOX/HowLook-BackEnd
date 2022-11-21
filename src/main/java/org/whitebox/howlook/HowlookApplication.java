package org.whitebox.howlook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HowlookApplication {
	public static void main(String[] args) {
		SpringApplication.run(HowlookApplication.class, args);
	}

}
