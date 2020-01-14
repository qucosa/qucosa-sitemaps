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

//import com.jayway.restassured.RestAssured;
//import com.jayway.restassured.http.ContentType;
import de.qucosa.repository.model.Url;
import de.qucosa.repository.model.UrlSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import static com.jayway.restassured.RestAssured.given;
//import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SitemapRestIT {
//    @Value("${server.port}")
//    private int restserverport;
//    @Value("${rest.server.host}")
//    private String restserverhost;
//    @LocalServerPort
//    private int port;
//
//    private static final String URLSETS_ENDPOINT = "/urlsets";
//    private static final String URLSET_SLUB_JSON = "/urlsets/testslub";
//    private static final String URLSET_UBL_JSON = "/urlsets/testubl";
//    private static final String URLSET_UBC_JSON = "/urlsets/testubc";
//
//    private final UrlSet urlset = new UrlSet("testslub");
//    private final UrlSet urlSet2 = new UrlSet("testubl");
//    private final UrlSet urlSet3 = new UrlSet("testubc");
//
//    private final Url url = new Url("https://example.com/landingpage1", "2018-10-10");
//    private final Url url2 = new Url("https://example.com/landingpage2", "2018-10-10");
//    private final Url url3 = new Url("https://example.com/landingpage3", "2018-10-10");
//    private final Url url4 = new Url("https://example.com/landingpage4", "2018-10-10");
//
//    @Before
//    public void setupData() {
//        RestAssured.port = port;
//        RestAssured.baseURI = restserverhost;
//        create_urlsets();
//        create_urls();
//    }
//
//    private void create_urlsets() {
//        given().contentType(ContentType.JSON).body(urlset).post(URLSETS_ENDPOINT);
//        given().contentType(ContentType.JSON).body(urlSet2).post(URLSETS_ENDPOINT);
//    }
//
//    private void create_urls() {
//        given().contentType(ContentType.JSON).body(url).post(URLSET_SLUB_JSON);
//        given().contentType(ContentType.JSON).body(url2).post(URLSET_SLUB_JSON);
//        given().contentType(ContentType.JSON).body(url3).post(URLSET_UBL_JSON);
//    }
//
//    @After
//    public void cleanupData() {
//        given().delete(URLSET_SLUB_JSON);
//        given().delete(URLSET_UBL_JSON);
//        given().delete(URLSET_UBC_JSON);
//    }
//
//    /**
//     * urlset-tests
//     * id-order (uri[]) in array alphabetical
//     */
//    @Ignore("position in body-arrays only works when restservice-repository is empty.")
//    @Test
//    public void is_urlset_persisted() {
//        given().headers("Content-Type", ContentType.JSON)
//                .when().get(URLSETS_ENDPOINT).then()
//                .body("uri[0]", equalTo("testslub"));
//
//        given().headers("Content-Type", ContentType.JSON)
//                .when().get(URLSETS_ENDPOINT).then()
//                .body("uri[1]", equalTo("testubl"));
//
//        given().headers("Content-Type", ContentType.JSON)
//                .when().get(URLSET_SLUB_JSON).then()
//                .body("uri", equalTo("testslub"));
//
//        given().headers("Content-Type", ContentType.JSON)
//                .when().get(URLSET_UBL_JSON).then()
//                .body("uri", equalTo("testubl"));
//    }
//
//    @Test
//    public void posting_urlset_responds_with_CREATED() {
//        given().contentType(ContentType.JSON).body(urlSet3)
//                .when().post(URLSETS_ENDPOINT).then()
//                .statusCode(equalTo(HttpStatus.CREATED.value()));
//    }
//
//    @Test
//    public void is_urlset_created() {
//        given().contentType(ContentType.JSON).body(urlSet3)
//                .when().post(URLSETS_ENDPOINT).then()
//                .statusCode(equalTo(HttpStatus.CREATED.value()));
//
//        given().headers("Content-Type", ContentType.JSON)
//                .get(URLSET_UBC_JSON).then()
//                .assertThat().body("uri", equalTo("testubc"))
//                .statusCode(equalTo(HttpStatus.OK.value()));
//    }
//
//    @Test
//    public void is_urlset_deleted() {
//        when().delete(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.NO_CONTENT.value()));
//
//        when().get(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()));
//    }
//
//    @Test
//    public void is_url_modified() {
//        url.setLastmod("2018-11-11");
//        /* modify valid url in correct urlset responds with OK*/
//        given().contentType(ContentType.JSON).body(url)
//                .when().put(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.OK.value()));
//
//        /* modify valid Url in wrong urlset responds with BAD_REQUEST */
//        given().contentType(ContentType.JSON).body(url)
//                .when().put(URLSET_UBL_JSON).then()
//                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
//
//        /* modify valid Url under nonexistent urlset responds with NOT_FOUND */
//        given().contentType(ContentType.JSON).body(url)
//                .when().put("/urlsets/notexistingurlset").then()
//                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()));
//
//        /* modify nonexistent Url responds with NOT_FOUND */
//        url.setLoc("abc");
//        given().contentType(ContentType.JSON).body(url)
//                .when().put(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()));
//
//        /* modify Url's without "loc"-Element responds with BAD_REQUEST */
//        url.setLoc("");
//        given().contentType(ContentType.JSON).body(url)
//                .when().put(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
//    }
//
//    /**
//     * url (children of urlsets) tests
//     * ToDo implement GET for urlset
//     */
//    @Test
//    @Ignore("array-matching is hard..")
//    public void is_url_persisted() {
////        given().headers("Content-Type", ContentType.JSON)
////                .when().get(URLSET_SLUB_JSON +"").then()
////                .body("urlList", arrayContaining("loc"));
//
//        given().headers("Content-Type", ContentType.JSON)
//                .when().get(URLSET_SLUB_JSON +"").then()
//                .body("urlList.flatten()", hasItems("loc"));
//
//        given().headers("Content-Type", ContentType.JSON)
//                .when().get(URLSET_SLUB_JSON +"/fooloc2").then()
//                .body("loc", equalTo("fooloc2"));
//
//        given().headers("Content-Type", ContentType.JSON)
//                .when().get(URLSET_UBL_JSON +"/fooloc3").then()
//                .body("loc", equalTo("fooloc3"));
//    }
//
//    @Test
//    public void posting_url_responds_with_CREATED() {
//        given().contentType(ContentType.JSON).body(url4)
//                .when().post(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.CREATED.value()));
//    }
//
//    @Test
//    public void is_url_created() {
//        given().contentType(ContentType.JSON).body(url4)
//                .when().post(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.CREATED.value()));
//    }
//
//    @Test
//    @Ignore("array-matching is hard..")
//    public void is_url_deleted() {
//        when().delete(URLSET_SLUB_JSON +"/fooloc").then()
//                .statusCode(equalTo(HttpStatus.NO_CONTENT.value()));
//        when().get(URLSET_SLUB_JSON + "/fooloc").then()
//                .statusCode(equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()));
//    }
//
//    @Test
//    public void get_sitemap_returns_xml() {
//        given().contentType(ContentType.XML)
//                .get(URLSET_SLUB_JSON).then()
//                .statusCode(equalTo(HttpStatus.OK.value()))
//                .contentType(ContentType.XML);
//    }
//
//    @Test
//    public void get_sitemap_index_returns_xml() {
//        given().contentType(ContentType.XML)
//                .get("/urlsets").then()
//                .statusCode(equalTo(HttpStatus.OK.value()))
//                .contentType(ContentType.XML);
//    }
}
