package de.qucosa.rest;

import de.qucosa.Application;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
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
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

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
public class SitemapControllerIT extends AbstractControllerIT {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Return sitemap xml by urlset tenant.")
    public void getSitemapXml() throws Exception {
        mvc.perform(
                get("/sitemap/ul")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(xpath("/urlset/url", null).nodeCount(greaterThan(0)));
    }

    @Test
    @DisplayName("Return empty sitemap xml because urlset does not exists.")
    public void emptySitemapXml() throws Exception {
        mvc.perform(
                get("/sitemap/test")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(xpath("/urlset/url", null).nodeCount(is(0)));
    }

    @Test
    @DisplayName("Return sitemap json by urlset tenat.")
    public void getSitemapJson() throws Exception {
        mvc.perform(
                get("/sitemap/ul")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Return empty sitemap json because urlset does not exists.")
    public void emptySitemapJson() throws Exception {
        mvc.perform(
                get("/sitemap/test")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Return urlsets sitemap.")
    public void getSitemapUrlSetsXml() throws Exception {
        mvc.perform(
                get("/sitemap")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(xpath("/sitemapindex/sitemap", null).nodeCount(greaterThan(0)))
                .andExpect(xpath("/sitemapindex/sitemap/loc", null).nodeCount(greaterThan(0)));
    }

    @Test
    @DisplayName("Return urlsets sitemap as json.")
    public void getSitemapUrlSetsJson() throws Exception {
        mvc.perform(
                get("/sitemap")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
