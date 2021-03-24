package ua.com.foxminded.galvad.university;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:database.properties")
public class SpringJdbcConfig {
	
	@Autowired
	Environment environment;

	private static final String URL = "url";
	private static final String USER = "dbuser";
	private static final String DRIVER = "driver";
	private static final String PASSWORD = "dbpassword";

	@Bean 
	public DataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUrl(environment.getProperty(URL));
		driverManagerDataSource.setUsername(environment.getProperty(USER));
		driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
		if (environment.getProperty(DRIVER)!=null) {
		driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));}
		return driverManagerDataSource;
	}

}
