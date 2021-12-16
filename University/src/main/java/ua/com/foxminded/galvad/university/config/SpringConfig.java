package ua.com.foxminded.galvad.university.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

	@Bean
	public ModelMapper getMapper() {
		return new ModelMapper();
	}

}
