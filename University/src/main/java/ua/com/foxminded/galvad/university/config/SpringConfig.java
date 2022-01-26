package ua.com.foxminded.galvad.university.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringConfig {

	@Bean
	public ModelMapper getMapper() {
		return new ModelMapper();
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(
				new Info().title("University API").version("1.0.0").contact(new Contact().email("v.halyanov@gmail.com")
						.url("https://www.linkedin.com/in/vadym-halyanov-1377144b/").name("Vadym Halyanov")));
	}
	
    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

}
