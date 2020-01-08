/*
 * Copyright (C) 2016 Saxon State and University Library Dresden (SLUB)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.qucosa;

import de.qucosa.repository.model.Url;
import de.qucosa.repository.model.Urlset;
import de.qucosa.utils.Utils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource("classpath:application-test.properties")
public class SitemapUnitTest {
//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    private UrlSetRepository urlSetRepository;
//    @Autowired
//    private UrlRepository urlRepository;
//
//    private final Url url = new Url("https://example.com/landingpage1", "2018-10-10");
//    private final Url url2 = new Url("https://example.com/landingpage2", "2018-10-10");
//    private final Url url3 = new Url("https://example.com/landingpage3", "2018-10-10");
//
//    private final Urlset urlset = new Urlset("slub");
//    private final Urlset urlset2 = new Urlset("ubl");
//    private final Urlset urlset3 = new Urlset("tuc");
//
//    @After
//    public void cleanup_setup() {
//        urlSetRepository.deleteAll();
//        urlRepository.deleteAll();
//    }
//
//    @Test
//    public void urlset_is_created() throws Exception {
//        create_urlset(urlset);
//        assertTrue("UrlsetRepository doesn't have item 'slub'"
//                , urlSetRepository.findById("slub").get().getUri().equals("slub"));
//    }
//
//    @Test
//    public void url_is_created() throws Exception {
//        create_urlset(urlset);
//        create_url(url, "slub");
//
//        assertTrue("UrlRepository doesn't have Url 'https://example.com/landingpage1'"
//                , urlRepository.findById(url.getLoc()).get().getLoc().equals("https://example.com/landingpage1"));
//        assertTrue("UrlsetRepository doesn't have item 'https://example.com/landingpage1' in slub-set."
//                , urlSetRepository.findById("slub").get().getUrlList().contains(url));
//    }
//
//    @Test
//    public void modifying_url_creates_urlset_if_not_existent() throws Exception {
//        modify_non_existent_url("slub", url);
//
//        assertTrue("Urlset 'slub' doesn't exist.", urlSetRepository.findById("slub").isPresent());
//    }
//
//    @Test
//    public void modify_non_existent_url_creates_url() throws Exception {
//        create_urlset(urlset);
//        modify_non_existent_url("slub", url);
//
//        assertTrue("UrlRepository doesn't have Url 'https://example.com/landingpage1'"
//                , urlRepository.findById(url.getLoc()).get().getLoc().equals(url.getLoc()));
//
//        url.setLastmod("2019-01-01");
//        modify_existent_url("slub", url);
//
//        assertTrue("UrlRepository doesn't have Url 'https://example.com/landingpage1'"
//                , urlRepository.findById(url.getLoc()).get().getLastmod().equals("2019-01-01"));
//    }
//
//    @Test
//    public void url_is_deleted() throws Exception {
//        create_urlset(urlset);
//        create_url(url, "slub");
//
//        assertTrue("UrlsetRepository doesn't have item "+url.getLoc()+" in Urlset "+ urlset.getUri()
//                , urlSetRepository.findById("slub").get().getUrlList().contains(url));
//
//        delete_url(url, "slub");
//
//        assertFalse("UrlsetRepository should not have item "+url.getLoc()+" after delete"
//                , urlSetRepository.findById("slub").get().getUrlList().contains(url));
//        assertFalse("UrlRepository should not have item "+url.getLoc()+" after delete"
//                , urlRepository.findAll().contains(url));
//    }
//
//    @Test
//    public void urlset_is_deleted() throws Exception {
//        create_urlset(urlset);
//
//        assertTrue("UrlsetRepository doesn't have urlset "+ urlset.getUri()
//                , urlSetRepository.findAll().contains(urlset));
//
//        delete_urlset("slub");
//
//        assertFalse("UrlsetRepository should not contain urlset " + urlset.getUri() +  "after delete."
//                , urlSetRepository.findAll().contains(urlset));
//    }
//
//    @Test
//    public void url_lastmod_date_is_modified() throws Exception {
//        create_urlset(urlset);
//        create_url(url, "slub");
//        Url createdUrl = urlRepository.findById(url.getLoc()).get();
//
//        assertTrue(createdUrl.getLastmod().equals("2018-10-10"));
//
//        //change lastmod
//        createdUrl.setLastmod("2018-11-11");
//        modify_existent_url("slub", createdUrl);
//
//        Url modifiedUrl = urlRepository.findById(url.getLoc()).get();
//        assertTrue(modifiedUrl.getLastmod().equals("2018-11-11"));
//    }
//
//    private void modify_existent_url(String urlsetname, Url url) throws Exception {
//        mvc.perform(put("/urlsets/" + urlsetname)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Utils.toJson(url)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//
//    private void modify_non_existent_url(String urlsetname, Url url) throws Exception {
//        mvc.perform(put("/urlsets/" + urlsetname)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Utils.toJson(url)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//
//    private void create_urlset(Urlset urlset) throws Exception {
//        mvc.perform(post("/urlsets")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Utils.toJson(urlset)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//
//    private void delete_url(Url url, String urlSetName) throws Exception {
//        mvc.perform(delete("/urlsets/" + urlSetName + "/deleteurl")
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(Utils.toJson(url)))
//                .andExpect(status().isNoContent());
//    }
//
//    private void delete_urlset(String urlsetName) throws Exception {
//        mvc.perform(delete("/urlsets/" + urlsetName)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Utils.toJson(urlset)))
//                .andExpect(status().isNoContent());
//    }
//
//    private void create_url(Url url, String urlsetName) throws Exception {
//        mvc.perform(post("/urlsets/" + urlsetName)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Utils.toJson(url)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }

}
