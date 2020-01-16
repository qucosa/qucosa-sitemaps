package de.qucosa.config;

import de.qucosa.repository.Dao;
import de.qucosa.repository.dao.UrlDao;
import de.qucosa.repository.dao.UrlSetDao;
import de.qucosa.repository.model.Url;
import de.qucosa.repository.model.UrlSet;
import de.qucosa.repository.services.UrlService;
import de.qucosa.repository.services.UrlSetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Configuration
public class ApplicationConfig {
    private final Environment environment;

    public ApplicationConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("app.db.url"));
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("app.db.driver")));
        dataSource.setUsername(environment.getProperty("app.db.username"));
        dataSource.setPassword(environment.getProperty("app.db.password"));
        return dataSource;
    }

    @Bean
    public Connection connection() throws SQLException {
        return dataSource().getConnection();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Dao<Url> urlDao() throws SQLException {
        return new UrlDao<>(connection());
    }

    @Bean
    public UrlService urlService() throws SQLException {
        UrlService urlService = new UrlService();
        urlService.setDao(urlDao());
        return urlService;
    }

    @Bean
    public Dao<UrlSet> urlSetDao() throws SQLException {
        return new UrlSetDao<>(connection());
    }

    @Bean
    public UrlSetService urlSetService() throws SQLException {
        UrlSetService urlSetService = new UrlSetService();
        urlSetService.setDao(urlSetDao());
        return urlSetService;
    }
}
