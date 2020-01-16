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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @DisplayName("Create a new url entry.")
    public void createUrl() throws Exception {
        mvc.perform(
                post("/url/test")
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
                post("/url/test")
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
        MvcResult mvcResult = mvc.perform(
                delete("/url/test")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("https://test.qucosa.de/id/qucosa:12146"))
                .andExpect(status().isNoContent()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
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
                post("/url/test")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg", is("Requestbody has to contain element loc.")));
    }

    @Test
    @Order(7)
    @DisplayName("Create failed because empty urlset parameter.")
    public void createFailed_2() throws Exception {
        mvc.perform(
                post("/url")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(status().reason(containsString("Request method 'POST' not supported")));
    }

    @Test
    @Order(8)
    @DisplayName("Nor found url because not exists url.")
    public void notFound() throws Exception {
        mvc.perform(
                get("/url/test?url=https://test.qucosa.de/id/qucosa:12146")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg", is("Cannot found url.")));
    }

    @Test
    @Order(9)
    @DisplayName("Delete failed because the urlset is does not exists.")
    public void deleteFailed() throws Exception {
        // delete exists test urlset
        mvc.perform(
                delete("/urlsets/test")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isNoContent());

        mvc.perform(
                delete("/url/test")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("https://test.qucosa.de/id/qucosa:12146"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg", is("Urlset test for url delete not found.")));
    }

    @Test
    @Order(10)
    @DisplayName("Nor found url because not exists url.")
    public void notFound_2() throws Exception {
        mvc.perform(
                get("/url/test")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg", is("Url collections size is 0.")));
    }

    @Test
    @Order(11)
    @DisplayName("Delete failed because the request body is empty.")
    public void deleteFailed_2() throws Exception {
        mvc.perform(
                delete("/url/test")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    @DisplayName("Delete failed because url loc does not exists.")
    public void deleteFailed_3() throws Exception {
        mvc.perform(
                delete("/url/ul")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("https://test.qucosa.de/id/qucosa:12146"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("0 url's from 1 removed."));
    }

    @AfterAll
    public void schutdwonTest() {
        sqlContainer.stop();
    }
}
