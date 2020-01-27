package de.qucosa.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.qucosa.Application;
import de.qucosa.repository.model.Url;
import de.qucosa.repository.services.UrlService;
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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
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
        classes = {Application.class, AbstractControllerTest.TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(initializers = {AbstractControllerTest.Initializer.class})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
public class UrlControllerTest extends AbstractControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UrlService urlService;

    @Autowired
    private MockMvc mvc;

    private final Url url = new Url();

    public UrlControllerTest() {
        url.setUrlSetUri("test");
        url.setLoc("https://test.qucosa.de/id/qucosa:12146");
    }

    @Test
    @Order(1)
    @DisplayName("Create a new url entry.")
    public void createUrl() throws Exception {
        mvc.perform(
                post("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.urlset_uri", is("test")))
                .andExpect(jsonPath("$.loc", is("https://test.qucosa.de/id/qucosa%3A12146")))
                .andExpect(jsonPath("$.lastmod", not(isEmptyString())));
    }

    @Test
    @Order(2)
    @DisplayName("Modify url with value in priority column.")
    public void updateUrl() throws Exception {
        url.setPriority("high");

        mvc.perform(
                post("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.urlset_uri", is("test")))
                .andExpect(jsonPath("$.loc", is("https://test.qucosa.de/id/qucosa%3A12146")))
                .andExpect(jsonPath("$.lastmod", not(isEmptyString())))
                .andExpect(jsonPath("$.priority", containsString("high")));
    }

    @Test
    @Order(3)
    @DisplayName("Find all urls by urlset test.")
    public void findAllUrlsByUrlset() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/url/test")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isFound()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        List<Url> urls = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Url.class));
        assertNotNull(urls);
        assertTrue(urls.size() > 0);
    }

    @Test
    @Order(4)
    @DisplayName("Delete url from table.")
    public void deleteUrl() throws Exception {
        Url url = new Url();
        url.setLoc("https://test.qucosa.de/id/qucosa:12146");
        url.setUrlSetUri("test");

        mvc.perform(
                delete("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @Order(5)
    @DisplayName("Find all urls.")
    public void findAllUrls() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isFound()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        List<Url> urls = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Url.class));
        assertNotNull(urls);
        assertTrue(urls.size() > 0);
    }

    @Test
    @Order(6)
    @DisplayName("Create failed because empty or null loc value in url object.")
    public void createFailed() throws Exception {
        url.setLoc("");
        mvc.perform(
                post("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg", is("Requestbody has to contain element loc.")));
    }

    @Test
    @Order(7)
    @DisplayName("Not found url because not exists url.")
    public void notFound() throws Exception {
        mvc.perform(
                get("/url/test?url=https://test.qucosa.de/id/qucosa:12146")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg", is("Cannot found url.")));
    }

    @Test
    @Order(8)
    @DisplayName("Not found url because not exists url.")
    public void notFound_2() throws Exception {
        mvc.perform(
                get("/url/test")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg", is("Url collections size is 0.")));
    }

    @Test
    @Order(9)
    @DisplayName("Delete failed because the request body is empty.")
    public void deleteFailed_2() throws Exception {
        mvc.perform(
                delete("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    @DisplayName("Delete failed because url loc does not exists.")
    public void deleteFailed_3() throws Exception {
        Url url = new Url();
        url.setLoc("https://test.qucosa.de/id/qucosa:12146");
        url.setUrlSetUri("test");

        mvc.perform(
                delete("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isNotAcceptable());
    }

    @AfterAll
    public void schutdwonTest() {
        sqlContainer.stop();
    }
}
