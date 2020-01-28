package de.qucosa.rest;

import com.github.dockerjava.api.command.CreateContainerCmd;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
import java.sql.SQLException;

public class AbstractControllerIT {

    @Container
    protected static final PostgreSQLContainer sqlContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:9.5")
            .withDatabaseName("qucosa_sitemap")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("insert-table-data.sql")
            .withStartupTimeoutSeconds(600)
            /* NOTE CreateContainerCmdModifier exposes internal API
                However, there is no better way to set the container name hence the feature
                request for a proper API has been dropped
                @see https://github.com/testcontainers/testcontainers-java/issues/254
             */
            .withCreateContainerCmdModifier(cmd -> ((CreateContainerCmd) cmd).withName("qucosa-sitemap-db"));

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            sqlContainer.start();

            TestPropertyValues.of(
                    "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + sqlContainer.getUsername(),
                    "spring.datasource.password=" + sqlContainer.getPassword()
            ).applyTo(configurableApplicationContext);
        }
    }

    @TestConfiguration(value = "classpath:application-dev.properties")
    public static class TestConfig {

        @Bean
        public Connection connection() throws SQLException {
            return sqlContainer.createConnection("");
        }
    }
}