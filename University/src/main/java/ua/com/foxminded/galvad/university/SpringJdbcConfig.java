package ua.com.foxminded.galvad.university;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("ua.com.foxminded.galvad.university")
@PropertySource("classpath:database.properties")
public class SpringJdbcConfig {
	
	@Value("${url}")
	private String url;
	@Value("${dbuser}")
	private String user;
	@Value("${driver}")
	private String driver;
	@Value("${dbpassword}")
	private String password;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(user);
		driverManagerDataSource.setPassword(password);
		if (driver != null) {
			driverManagerDataSource.setDriverClassName(driver);
		}
		return driverManagerDataSource;
	}
}
