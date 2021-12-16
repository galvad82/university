package ua.com.foxminded.galvad.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class UniversityApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UniversityApplication.class, args);
	}

}
