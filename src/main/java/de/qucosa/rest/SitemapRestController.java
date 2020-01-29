/*
 * Copyright 2017 Saxon State and University Library Dresden (SLUB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package de.qucosa.rest;

import de.qucosa.model.SitemapIndexModel;
import de.qucosa.model.SitemapModel;
import de.qucosa.repository.model.Url;
import de.qucosa.repository.model.UrlSet;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/sitemap")
public class SitemapRestController extends ControllerAbstract {

    private final RestTemplate restTemplate;

    public SitemapRestController( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{urlset}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapForTenant(@PathVariable("urlset") String urlset, HttpServletRequest request) {
        SitemapModel tenantSitemap = new SitemapModel();

        try {
            Collection<Url> urls = restTemplate.exchange(
                    serverUrl(request, serverPort) + "/url/" + urlset,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<Url>>(){}).getBody();

            tenantSitemap.setUrl(urls);

            return new ResponseEntity<>(tenantSitemap, HttpStatus.OK);
        } catch (HttpClientErrorException ignored) { }

        return new ResponseEntity<>(tenantSitemap, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{urlset}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity getSitemapforTenantAsJson(@PathVariable("urlset") String urlset,  HttpServletRequest request) {

        try {
            Collection<Url> urls = restTemplate.exchange(
                    serverUrl(request, serverPort) + "/url/" + urlset,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<Url>>() {
                    }).getBody();

            return new ResponseEntity<>(urls, HttpStatus.OK);
        } catch (HttpClientErrorException ignored) { }

        return new ResponseEntity<>("[]", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapIndex(HttpServletRequest request) {
        SitemapIndexModel sitemapIndex = new SitemapIndexModel();

        try {
            Collection<UrlSet> urlSets = restTemplate.exchange(
                    serverUrl(request, serverPort) + "/urlsets",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<UrlSet>>() {
                    }).getBody();

            sitemapIndex.setUrlset(urlSets);
            return new ResponseEntity<>(sitemapIndex, HttpStatus.OK);
        } catch (HttpClientErrorException ignored) { }

        return new ResponseEntity<>(sitemapIndex, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapIndexAsJson(HttpServletRequest request) {

        try {
            Collection<UrlSet> urlSets = restTemplate.exchange(
                    serverUrl(request, serverPort) + "/urlsets",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<UrlSet>>() {
                    }).getBody();
            return new ResponseEntity<>(urlSets, HttpStatus.OK);
        } catch (HttpClientErrorException ignored) { }

        return new ResponseEntity<>("[]", HttpStatus.OK);
    }
}
