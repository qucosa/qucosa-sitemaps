/*
 * Copyright 2017 Saxon State and University Library Dresden (SLUB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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
