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

import de.qucosa.model.SitemapModel;
import de.qucosa.repository.model.Url;
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
                serverUrl(request) + "/url/" + urlset,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Url>>(){}).getBody();

        SitemapModel tenantSitemap = new SitemapModel();
        tenantSitemap.setUrl(urls);

        return new ResponseEntity<>(tenantSitemap, HttpStatus.OK);
    }

    /**
     * urlSet-operation to
     * get specific tenant-sitemap (UrlSet) as XML
     * listens to GET-Requests with any content-type but "application/json"
     */
//    @RequestMapping(method = GET, value = "/urlsets/{urlSetName}", produces = MediaType.APPLICATION_XML_VALUE)
//    @ResponseBody
//    public ResponseEntity getSitemapForTenant(@PathVariable("urlSetName") String urlSetName) {
//        SitemapModel tenantSitemap = new SitemapModel();
//
//        Optional<Urlset> urlset = urlSetRepository.findById(urlSetName);
//        if (!urlset.isPresent()) {
//            return new ResponseEntity<>("Urlset / Tenant: " + urlSetName + " doesn't exist."
//                    , HttpStatus.NOT_FOUND);
//        }
//
//        tenantSitemap.setUrl(urlset.get().getUrlList());
//
//        return new ResponseEntity<>(tenantSitemap, HttpStatus.OK);
//    }

    /**
     * urlSet-operation to
     * get specific tenant-sitemap (UrlSet) as JSON
     * listens to GET-Requests with content-type="application/json"
     */
//    @RequestMapping(method = GET, value = "/urlsets/{urlSetName}", consumes = MediaType.APPLICATION_JSON_VALUE
//            , produces = {MediaType.APPLICATION_JSON_VALUE})
//    @ResponseBody
//    public ResponseEntity getSitemapforTenantAsJson(@PathVariable("urlSetName") String urlSetName) {
//        Optional<Urlset> urlset = urlSetRepository.findById(urlSetName);
//        if (!urlset.isPresent()) {
//            return new ResponseEntity<>("{\"message\": \"Urlset / Tenant '" + urlSetName + "' doesn't exist.\"}"
//                    , HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(urlset.get(), HttpStatus.OK);
//    }


    /**
     * sitemap-operation to
     * get sitemapindex (all urlSets) as XML
     * responds with locations to every tenant sitemap
     * listens to GET-Requests with any content-type but "application/json"
     */
//    @RequestMapping(method = GET, value = "/urlsets"
//            , produces = MediaType.APPLICATION_XML_VALUE)
//    @ResponseBody
//    public ResponseEntity getSitemapIndex() {
//        SitemapIndexModel sitemapIndex = new SitemapIndexModel();
//        sitemapIndex.setUrlset(urlSetRepository.findAll());
//
//        return new ResponseEntity<>(sitemapIndex, HttpStatus.OK);
//    }

    /**
     * sitemap-operation to
     * get sitemapindex (all urlSets) as JSON
     * listens to GET-Requests with content-type="application/json"
     */
//    @RequestMapping(method = GET, value = "/urlsets", consumes = MediaType.APPLICATION_JSON_VALUE
//            , produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity getSitemapIndexAsJson() {
//        List<Urlset> allUrlsets = urlSetRepository.findAll();
//        return new ResponseEntity<>(allUrlsets, HttpStatus.OK);
//    }
}
