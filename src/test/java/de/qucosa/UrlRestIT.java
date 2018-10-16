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

    @Before
    public void setupData() {
        Urlset urlset = new Urlset("my-site");
        Urlset urlset2 = new Urlset("my-secondsite");

        given().contentType(ContentType.JSON).body(urlset).post(USER_ENDPOINT);
        given().contentType(ContentType.JSON).body(urlset2).post(USER_ENDPOINT);

        Url url = new Url("fooloc", "2018-10-10");
        Url url2 = new Url("fooloc2", "2018-10-10");
        Url url3 = new Url("fooloc3", "2018-10-10");

        given().contentType(ContentType.JSON).body(url).post("/urlsets/my-site");
        given().contentType(ContentType.JSON).body(url2).post("/urlsets/my-site");
        given().contentType(ContentType.JSON).body(url3).post("/urlsets/my-secondsite");
    }

    @Test
    public void setupTest() {
        System.out.println("test");
    }

    @Test
    public void create_urlset() {
        Urlset urlset = new Urlset("my-site");

        given().contentType(ContentType.JSON).body(urlset)
                .when().post(USER_ENDPOINT).then()
                .assertThat().statusCode(equalTo(HttpStatus.CREATED.value()));
    }

    @Test
    public void persist_created_urlset() {
        given().headers("Content-Type", ContentType.JSON)
                .when().get(USER_ENDPOINT).then()
                .body("uri[0]", equalTo("my-site"));

        when().get(USER_ENDPOINT).then()
                .statusCode(equalTo(HttpStatus.OK.value()));
    }

}
