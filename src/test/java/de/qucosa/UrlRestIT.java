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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.post;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UrlRestIT {
    private static final String USER_ENDPOINT = "/urlsets";

    private Urlset urlset = new Urlset("my-site");
    private Urlset urlset2 = new Urlset("my-secondsite");
    private Urlset urlset3 = new Urlset("my-thirdsite");

    private Url url = new Url("fooloc", "2018-10-10");
    private Url url2 = new Url("fooloc2", "2018-10-10");
    private Url url3 = new Url("fooloc3", "2018-10-10");
    private Url url4 = new Url("fooloc4", "2018-10-10");

    @Before
    public void setupData() {
        create_urlsets();
        create_urls();
    }

    private void create_urlsets() {
        given().contentType(ContentType.JSON).body(urlset).post(USER_ENDPOINT);
        given().contentType(ContentType.JSON).body(urlset2).post(USER_ENDPOINT);
    }

    private void create_urls() {
        given().contentType(ContentType.JSON).body(url).post("/urlsets/my-site");
        given().contentType(ContentType.JSON).body(url2).post("/urlsets/my-site");
        given().contentType(ContentType.JSON).body(url3).post("/urlsets/my-secondsite");
    }

    /**
     * urlset-tests
     */
    @Test
    public void is_urlset_persisted() {
        given().headers("Content-Type", ContentType.JSON)
                .when().get(USER_ENDPOINT).then()
                .body("uri[0]", equalTo("my-site"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get(USER_ENDPOINT).then()
                .body("uri[1]", equalTo("my-secondsite"));
    }

    @Test
    public void posting_urlset_responds_with_CREATED() {
        given().contentType(ContentType.JSON).body(urlset3)
                .when().post(USER_ENDPOINT).then()
                .assertThat().statusCode(equalTo(HttpStatus.CREATED.value()));
    }

    @Test
    public void is_urlset_deleted() {
        when().delete("urlsets/my-site").then()
                .statusCode(equalTo(HttpStatus.NO_CONTENT.value()));

        when().get("urlsets/my-site").then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()));
    }

    /**
     * url (children of urlsets) tests
     */
    @Test
    public void is_url_persisted() {
        given().headers("Content-Type", ContentType.JSON)
                .when().get("urlsets/my-site/fooloc").then()
                .body("loc[0]", equalTo("fooloc"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get("urlsets/my-site/fooloc2").then()
                .body("loc[0]", equalTo("fooloc2"));

        given().headers("Content-Type", ContentType.JSON)
                .when().get("urlsets/my-secondsite/fooloc3").then()
                .body("loc[0]", equalTo("fooloc3"));
    }

    @Test
    public void posting_url_responds_with_CREATED() {
        given().contentType(ContentType.JSON).body(url4)
                .when().post("urlsets/my-site").then()
                .assertThat().statusCode(equalTo(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    public void is_url_deleted() {
        System.out.println("test");
        when().delete("urlsets/my-site/fooloc").then()
                .statusCode(equalTo(HttpStatus.NO_CONTENT.value()));

        when().delete("urlsets/my-site/dummyloc").then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()));
    }
}
