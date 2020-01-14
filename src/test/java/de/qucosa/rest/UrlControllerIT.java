package de.qucosa.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.qucosa.Application;
import de.qucosa.repository.model.Url;
import de.qucosa.repository.services.UrlService;
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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties= {"spring.main.allow-bean-definition-overriding=true"},
        classes = {Application.class, AbstractControllerIT.TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(initializers = {AbstractControllerIT.Initializer.class})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
public class UrlControllerIT extends AbstractControllerIT {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UrlService urlService;

    @Autowired
    private MockMvc mvc;

    Url url = new Url();

    public UrlControllerIT() {
        url.setUrlSetUri("test");
        url.setLoc("https://test.qucosa.de/id/qucosa:12146");
    }

    @Test
    @Order(1)
    @DisplayName("Find all urls.")
    public void findAllUrlsByUrlset() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/url/slub")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isFound()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        List<Url> urls = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Url.class));
        assertNotNull(urls);
        assertTrue(urls.size() > 0);
    }

    @Test
    @Order(2)
    @DisplayName("Create a new url entry.")
    public void createUrl() {

    }
}
