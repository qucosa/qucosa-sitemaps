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

import com.jayway.restassured.http.ContentType;
import de.qucosa.model.Url;
import de.qucosa.model.Urlset;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class UrlRestIT {
    private static final String URLSETS_ENDPOINT = "/urlsets";
    private static final String URLSET_SLUB_JSON = "/urlsets/slub";
    private static final String URLSET_UBL_JSON = "/urlsets/ubl";
    private static final String URLSET_UBC_JSON = "/urlsets/ubc";

    private Urlset urlset = new Urlset("slub");
    private Urlset urlset2 = new Urlset("ubl");
    private Urlset urlset3 = new Urlset("ubc");

    private Url url = new Url("https://example.com/landingpage1", "2018-10-10");
    private Url url2 = new Url("https://example.com/landingpage2", "2018-10-10");
    private Url url3 = new Url("https://example.com/landingpage3", "2018-10-10");
    private Url url4 = new Url("https://example.com/landingpage4", "2018-10-10");

    @Before
    public void setupData() {
        create_urlsets();
        create_urls();
    }

    private void create_urlsets() {
        given().contentType(ContentType.JSON).body(urlset).post(URLSETS_ENDPOINT);
        given().contentType(ContentType.JSON).body(urlset2).post(URLSETS_ENDPOINT);
    }

    private void create_urls() {
        given().contentType(ContentType.JSON).body(url).post(URLSET_SLUB_JSON);
        given().contentType(ContentType.JSON).body(url2).post(URLSET_SLUB_JSON);
        given().contentType(ContentType.JSON).body(url3).post(URLSET_UBL_JSON);
    }

    @After
    public void cleanupData() {
        given().delete(URLSET_SLUB_JSON);
        given().delete(URLSET_UBL_JSON);
        given().delete(URLSET_UBC_JSON);
    }

    /**
     * urlset-tests
     * id-order (uri[]) in array alphabetical
     */
    @Test
    public void is_urlset_persisted() {
        given().headers("Content-Type", ContentType.JSON)
                .when().get(URLSETS_ENDPOINT).then()
                .body("uri[0]", equalTo("slub"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get(URLSETS_ENDPOINT).then()
                .body("uri[1]", equalTo("ubl"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get(URLSET_SLUB_JSON).then()
                .body("uri", equalTo("slub"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get(URLSET_UBL_JSON).then()
                .body("uri", equalTo("ubl"));
    }

    @Test
    public void posting_urlset_responds_with_CREATED() {
        given().contentType(ContentType.JSON).body(urlset3)
                .when().post(URLSETS_ENDPOINT).then()
                .statusCode(equalTo(HttpStatus.CREATED.value()));
    }

    @Test
    public void is_urlset_created() {
        given().contentType(ContentType.JSON).body(urlset3)
                .when().post(URLSETS_ENDPOINT).then()
                .statusCode(equalTo(HttpStatus.CREATED.value()));

        given().headers("Content-Type", ContentType.JSON)
                .get(URLSET_UBC_JSON).then()
                .assertThat().body("uri", equalTo("ubc"))
                .statusCode(equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void is_urlset_deleted() {
        when().delete(URLSET_SLUB_JSON).then()
                .statusCode(equalTo(HttpStatus.NO_CONTENT.value()));

        when().get(URLSET_SLUB_JSON).then()
                .statusCode(equalTo(HttpStatus.NOT_ACCEPTABLE.value()));
    }

    /**
     * url (children of urlsets) tests
     * ToDo GET doesn't work for absolute uri's (url's as url parameter)
     */
    @Test
    @Ignore("url's as url parameter don't work")
    public void is_url_persisted() {
        given().headers("Content-Type", ContentType.JSON)
                .when().get(URLSET_SLUB_JSON +"/fooloc").then()
                .body("loc", equalTo("fooloc"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get(URLSET_SLUB_JSON +"/fooloc2").then()
                .body("loc", equalTo("fooloc2"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get(URLSET_UBL_JSON +"/fooloc3").then()
                .body("loc", equalTo("fooloc3"));
    }

    @Test
    public void posting_url_responds_with_CREATED() {
        given().contentType(ContentType.JSON).body(url4)
                .when().post(URLSET_SLUB_JSON).then()
                .statusCode(equalTo(HttpStatus.CREATED.value()));
    }

    @Test
    public void is_url_created() {
        given().contentType(ContentType.JSON).body(url4)
                .when().post(URLSET_SLUB_JSON).then()
                .statusCode(equalTo(HttpStatus.CREATED.value()));

//        when().get(URLSET_SLUB_JSON +"/fooloc4").then()
//                .body("loc", equalTo("fooloc4"))
//                .statusCode(equalTo(HttpStatus.OK.value()));
    }

    @Test
    @Ignore("url's as url parameter don't work")
    public void is_url_deleted() {
        when().delete(URLSET_SLUB_JSON +"/fooloc").then()
                .statusCode(equalTo(HttpStatus.NO_CONTENT.value()));
        when().get(URLSET_SLUB_JSON + "/fooloc").then()
                .statusCode(equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
