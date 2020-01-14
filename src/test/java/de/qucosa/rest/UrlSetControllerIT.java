package de.qucosa.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.CreateContainerCmd;
import de.qucosa.Application;
import de.qucosa.repository.model.UrlSet;
import de.qucosa.repository.services.UrlSetService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties= {"spring.main.allow-bean-definition-overriding=true"},
        classes = {Application.class, UrlSetControllerIT.TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(initializers = {UrlSetControllerIT.Initializer.class})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
public class UrlSetControllerIT {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UrlSetService urlSetService;

    @Autowired
    private MockMvc mvc;

    private UrlSet urlSet = new UrlSet();

    public UrlSetControllerIT() {
        urlSet.setUri("test");
    }

    @Container
    private static final PostgreSQLContainer sqlContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:9.5")
            .withDatabaseName("qucosa_sitemap")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("insert-table-data.sql")
            .withStartupTimeoutSeconds(600)
            .withCreateContainerCmdModifier(new Consumer<CreateContainerCmd>() {
                @Override
                public void accept(CreateContainerCmd createContainerCmd) {
                    createContainerCmd.withName("qucosa-sitemap-db");
                }
            });

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

    @Test
    @Order(1)
    @DisplayName("Find all persistent saved urlsets.")
    public void findAllUrlSets() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/urlsets")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isFound()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        List<UrlSet> urlSets = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, UrlSet.class));
        assertNotNull(urlSets);
        assertTrue(urlSets.size() > 0);
    }

    @Test
    @Order(2)
    @DisplayName("Create new urlset.")
    public void createUrlSet() throws Exception {
        mvc.perform(
                post("/urlsets")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(urlSet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uri", not(isEmptyString())))
                .andExpect(jsonPath("$.lastmod", not(isEmptyString())))
                .andExpect(jsonPath("$.loc", not(isEmptyString())))
                .andExpect(jsonPath("$.loc", containsString("urlsets")))
                .andExpect(jsonPath("$.loc", containsString("test")));
    }

    @Test
    @Order(3)
    @DisplayName("Find urlset by uri.")
    public void findUrlSet() throws Exception {
        mvc.perform(
                get("/urlsets/test")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.uri", not(isEmptyString())))
                .andExpect(jsonPath("$.lastmod", not(isEmptyString())))
                .andExpect(jsonPath("$.loc", not(isEmptyString())))
                .andExpect(jsonPath("$.loc", containsString("urlsets")))
                .andExpect(jsonPath("$.loc", containsString("test")));
    }

    @Test
    @Order(4)
    @DisplayName("Delete urlset by uri.")
    public void deleteUrlSets() throws Exception {
        MvcResult mvcResult = mvc.perform(
                delete("/urlsets/test")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response, is("Urlset / Tenant test is deleted."));
    }

    @AfterAll
    public void schutdwonTest() {
        sqlContainer.stop();
    }
}
