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
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/sitemap")
public class SitemapRestController extends ControllerAbstract {

    private RestTemplate restTemplate;

    public SitemapRestController( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{urlset}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapForTenant(@PathVariable("urlset") String urlset, HttpServletRequest request) {
        Collection<Url> urls = restTemplate.exchange(
                serverUrl(request, serverPort) + "/url/" + urlset,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Url>>(){}).getBody();

        SitemapModel tenantSitemap = new SitemapModel();
        tenantSitemap.setUrl(urls);

        return new ResponseEntity<>(tenantSitemap, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{urlset}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity getSitemapforTenantAsJson(@PathVariable("urlset") String urlset,  HttpServletRequest request) {
        Collection<Url> urls = restTemplate.exchange(
                serverUrl(request, serverPort) + "/url/" + urlset,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Url>>(){}).getBody();

        return new ResponseEntity<>(urls, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapIndex(HttpServletRequest request) {
        Collection<UrlSet> urlSets = restTemplate.exchange(
                serverUrl(request, serverPort) + "/urlsets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<UrlSet>>(){}).getBody();

        SitemapIndexModel sitemapIndex = new SitemapIndexModel();
        sitemapIndex.setUrlset(urlSets);

        return new ResponseEntity<>(sitemapIndex, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapIndexAsJson(HttpServletRequest request) {
        Collection<UrlSet> urlSets = restTemplate.exchange(
                serverUrl(request, serverPort) + "/urlsets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<UrlSet>>(){}).getBody();
        return new ResponseEntity<>(urlSets, HttpStatus.OK);
    }
}
