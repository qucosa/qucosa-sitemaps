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

package de.qucosa.sitemap;

import de.qucosa.model.SitemapIndexModel;
import de.qucosa.model.SitemapModel;
import de.qucosa.model.Url;
import de.qucosa.model.Urlset;
import de.qucosa.repository.UrlRepository;
import de.qucosa.repository.UrlSetRepository;
import de.qucosa.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class SitemapRestController {

    private int restserverport;
    private String restserverhost;
    private UrlSetRepository urlSetRepository;
    private UrlRepository urlRepository;
    // to get server port
    private Environment environment;

    @Autowired
    public SitemapRestController(UrlSetRepository urlSetRepository, UrlRepository urlRepository, Environment environment
        , @Value("${rest.server.host}") final String restserverhost, @Value("${server.port}") final int restserverport) {
        this.urlSetRepository = urlSetRepository;
        this.urlRepository = urlRepository;
        this.environment = environment;
        this.restserverhost = restserverhost;
        this.restserverport = restserverport;
    }

    private String getHostUrl() {
        String hosturl;
        if (!Utils.empty(restserverhost)) {
            hosturl = restserverhost + ":" + restserverport + "/";
        } else {
            String ip = InetAddress.getLoopbackAddress().getHostAddress();
            hosturl = ip+":"+environment.getProperty("local.server.port")+"/";
        }
        return hosturl;
    }

    /**
     * url-operation to
     * create url in given urlSetName
     */
    @RequestMapping(method = POST, value = "/urlsets/{urlSetName}", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity createUrl(@PathVariable("urlSetName") String urlSetName, @RequestBody Url url) {
        if (Utils.empty(url.getLoc())) {
            return new ResponseEntity<>("{\"message\": \"Requestbody has to contain Element 'loc'\"}"
                    , HttpStatus.BAD_REQUEST);
        }
        // set lastmod to current time if not given
        if (Utils.empty(url.getLastmod())) {
            url.setLastmod(Utils.getCurrentW3cDatetime());
        }

        // create Urlset if it doesn't exist
        Optional<Urlset> containgUrlSet = urlSetRepository.findById(urlSetName);

        if (!containgUrlSet.isPresent()) {
            // set uri / tenant-name
            Urlset urlsetToCreate = new Urlset(urlSetName);
            // set location
            urlsetToCreate.setLoc(getHostUrl() + "urlsets/" + urlSetName);
            // set lastmod
            if (url.getLastmod().isEmpty()) {
                url.setLastmod(Utils.getCurrentW3cDatetime());
            }

            urlSetRepository.save(urlsetToCreate);
        }

        Urlset actualUrlset = urlSetRepository.findById(urlSetName).get();
        url.setUrlset(actualUrlset);

        urlRepository.save(url);
        actualUrlset.getUrlList().add(url);
        actualUrlset.setLastmod(Utils.getCurrentW3cDatetime());
        urlSetRepository.save(actualUrlset);

        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

    /**
     * url-operation to
     * modify/update url in given urlSetName
     */
    @RequestMapping(method = PUT, value = "/urlsets/{urlSetName}", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity modifyUrl(@PathVariable("urlSetName") String urlSetName, @RequestBody Url url) {
        if (Utils.empty(url.getLoc())) {
            return new ResponseEntity<>("{\"message\": \"Requestbody has to contain Element 'loc'\"}"
                    , HttpStatus.BAD_REQUEST);
        }

        // create Urlset if it doesn't exist
        Optional<Urlset> containgUrlSet = urlSetRepository.findById(urlSetName);

        if (!containgUrlSet.isPresent()) {
            // set uri / tenant-name
            Urlset urlsetToCreate = new Urlset(urlSetName);
            // set location
            urlsetToCreate.setLoc(getHostUrl() + "urlsets/" + urlSetName);
            // set lastmod
            urlsetToCreate.setLastmod(Utils.getCurrentW3cDatetime());

            urlSetRepository.save(urlsetToCreate);
        }

        Urlset actualUrlset = urlSetRepository.findById(urlSetName).get();

        Optional<Url> urlToBeModified = urlRepository.findById(url.getLoc());
        if (!urlToBeModified.isPresent()) {
            url.setUrlset(actualUrlset);
            // set lastmod to current time if not given
            if (Utils.empty(url.getLastmod())) {
                url.setLastmod(Utils.getCurrentW3cDatetime());
            }

            urlRepository.save(url);
            actualUrlset.addUrl(url);
            actualUrlset.getUrlList().add(url);
            actualUrlset.setLastmod(Utils.getCurrentW3cDatetime());
            urlSetRepository.save(actualUrlset);

            return new ResponseEntity<>("{\"message\": \"Url could not be modified, because it did not exist." +
                    " Therefore created Url: " + url.getLoc() + "\"}", HttpStatus.CREATED);
        } else {
            Url actualUrl = urlToBeModified.get();

            actualUrl.setUrlset(actualUrlset);
            // set lastmod to current time if not given
            if (!Utils.empty(url.getLastmod())) {
                actualUrl.setLastmod(url.getLastmod());
            } else {
                actualUrl.setLastmod(Utils.getCurrentW3cDatetime());
            }
            if (!Utils.empty(url.getChangefreq())) {
                actualUrl.setChangefreq(url.getChangefreq());
            }
            if (!Utils.empty(url.getPriority())) {
                actualUrl.setPriority(url.getPriority());
            }

            urlRepository.save(actualUrl);
            actualUrlset.getUrlList().add(actualUrl);
            urlSetRepository.save(actualUrlset);

            return new ResponseEntity<>(actualUrl, HttpStatus.OK);
        }
    }

    /**
     * url-operation to
     * delete url in given urlSetName
     */
    /* TODO test bulk-delete */
    /* TODO spring security authorization einbauen */
    @RequestMapping(method = DELETE, value = "/urlsets/{urlSetName}/deleteurl", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity deleteUrl(@PathVariable("urlSetName") String urlSetName, @RequestBody List<String> urlList) {
        Optional<Urlset> setInRepo = urlSetRepository.findById(urlSetName);
        if (!setInRepo.isPresent()) {
            return new ResponseEntity<>("Urlset (tenant) " + urlSetName + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        boolean anyUrlDeleted = false;
        for (String url : urlList) {
            Optional<Url> urlInRepo  = urlRepository.findById(url);
            if (urlInRepo.isPresent()) {
                setInRepo.get().setLastmod(Utils.getCurrentW3cDatetime());
                // TODO test if needed
//                setInRepo.get().getUrlList().remove(urlInRepo.get());

                urlRepository.delete(urlInRepo.get());
                anyUrlDeleted = true;
            }
        }
        if (anyUrlDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("None of the Url's could be found in the sitemap-repository"
                    , HttpStatus.NOT_FOUND);
        }

    }

    /**
     * url-operation to
     * delete url in given urlSetName
     */
    @RequestMapping(method = DELETE, value = "/urlsets/{urlSetName}/deleteurl", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity deleteUrl(@PathVariable("urlSetName") String urlSetName, @RequestBody Url url) {
        if (Utils.empty(url.getLoc())) {
            return new ResponseEntity<>("Requestbody has to contain Element 'loc'", HttpStatus.BAD_REQUEST);
        }

        Optional<Urlset> containgUrlSet = urlSetRepository.findById(urlSetName);
        if (!containgUrlSet.isPresent()) {
            return new ResponseEntity<>("Urlset (tenant) " + urlSetName + " doesn't exist", HttpStatus.NOT_FOUND);
        }

        Optional<Url> urlToBeModified = urlRepository.findById(url.getLoc());
        if (!urlToBeModified.isPresent()) {
            return new ResponseEntity<>("Url " + url.getLoc() + " doesn't exist", HttpStatus.NOT_FOUND);
        }

        Urlset setInRepo = containgUrlSet.get();
        Url urlInRepo = urlToBeModified.get();

        // Url-location is part of the Urlset (tenant) in url (urlSetName}
        if (setInRepo.getUrlList().contains(urlInRepo)) {
            urlRepository.delete(urlInRepo);
            setInRepo.setLastmod(Utils.getCurrentW3cDatetime());
            setInRepo.getUrlList().remove(urlInRepo);
            urlSetRepository.save(setInRepo);

            return new ResponseEntity<>("{\"message\": \"URL deleted.\"",HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("{\"message\": \"Url '" + urlInRepo.getLoc() + "' doesn't exist in urlset (tenant) "
                    + urlSetName + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * url-operation to
     * get url in given urlSetName
     * TODO url as parameter doesn't work
     */
    /*
    @RequestMapping(method = GET, value = "/urlsets/{urlSetName}/{url}")
    @ResponseBody
    public ResponseEntity getUrl(@PathVariable("urlSetName") String urlSetName, @PathVariable("url") String url) {
        return new ResponseEntity<>(urlRepository.findById(url).get(), HttpStatus.OK);
    }
    */

    /**
     * urlset-operation to
     * create urlSets
     */
    @RequestMapping(method = POST, value = "/urlsets", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity createUrlSet(@RequestBody Urlset urlset) {
        Optional<Urlset> urlsetToCreate = urlSetRepository.findById(urlset.getUri());
        if (urlsetToCreate.isPresent()) {
            return new ResponseEntity<>("{\"message\": \"Urlset " + urlset.getUri()
                    + " is already created.\"}", HttpStatus.ALREADY_REPORTED);
        }

        /* TODO hostauflösung überprüfen */
        urlset.setLoc(getHostUrl() + "urlsets/" + urlset.getUri());
        urlset.setLastmod(Utils.getCurrentW3cDatetime());
        urlSetRepository.save(urlset);

        return new ResponseEntity<>(urlset, HttpStatus.CREATED);
    }

    /**
     * urlSet-operation to
     * get specific tenant-sitemap (UrlSet) as XML
     * listens to GET-Requests with any content-type but "application/json"
     */
    @RequestMapping(method = GET, value = "/urlsets/{urlSetName}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapForTenant(@PathVariable("urlSetName") String urlSetName) {
        SitemapModel tenantSitemap = new SitemapModel();

        Optional<Urlset> urlset = urlSetRepository.findById(urlSetName);
        if (!urlset.isPresent()) {
            return new ResponseEntity<>("Urlset / Tenant: " + urlSetName + " doesn't exist."
                    , HttpStatus.NOT_FOUND);
        }

        tenantSitemap.setUrl(urlset.get().getUrlList());

        return new ResponseEntity<>(tenantSitemap, HttpStatus.OK);
    }

    /**
     * urlSet-operation to
     * get specific tenant-sitemap (UrlSet) as JSON
     * listens to GET-Requests with content-type="application/json"
     */
    @RequestMapping(method = GET, value = "/urlsets/{urlSetName}", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity getSitemapforTenantAsJson(@PathVariable("urlSetName") String urlSetName) {
        Optional<Urlset> urlset = urlSetRepository.findById(urlSetName);
        if (!urlset.isPresent()) {
            return new ResponseEntity<>("{\"message\": \"Urlset / Tenant '" + urlSetName + "' doesn't exist.\"}"
                    , HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(urlset.get(), HttpStatus.OK);
    }

    /**
     * urlSet-operation to
     * delete specific urlset, identified by setname
     */
    @RequestMapping(method = DELETE, value = "/urlsets/{urlSetName}")
    @ResponseBody
    public ResponseEntity deleteUrlSet(@PathVariable("urlSetName") String urlSetName) {
        Optional<Urlset> urlset = urlSetRepository.findById(urlSetName);
        if (!urlset.isPresent()) {
            return new ResponseEntity<>("Urlset / Tenant '" + urlSetName + "' doesn't exist."
                    , HttpStatus.NOT_FOUND);
        }

        urlSetRepository.delete(urlset.get());
        return new ResponseEntity<>("Urlset / Tenant '" + urlSetName + "' deleted.", HttpStatus.NO_CONTENT);
    }

    /**
     * sitemap-operation to
     * get sitemapindex (all urlSets) as XML
     * responds with locations to every tenant sitemap
     * listens to GET-Requests with any content-type but "application/json"
     */
    @RequestMapping(method = GET, value = "/urlsets"
            , produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapIndex() {
        SitemapIndexModel sitemapIndex = new SitemapIndexModel();
        sitemapIndex.setUrlset(urlSetRepository.findAll());

        return new ResponseEntity<>(sitemapIndex, HttpStatus.OK);
    }

    /**
     * sitemap-operation to
     * get sitemapindex (all urlSets) as JSON
     * listens to GET-Requests with content-type="application/json"
     */
    @RequestMapping(method = GET, value = "/urlsets", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getSitemapIndexAsJson() {
        List<Urlset> allUrlsets = urlSetRepository.findAll();
        return new ResponseEntity<>(allUrlsets, HttpStatus.OK);
    }
}
