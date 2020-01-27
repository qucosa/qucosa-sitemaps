package de.qucosa.rest;

import de.qucosa.ErrorDetails;
import de.qucosa.repository.exceptions.DeleteFailed;
import de.qucosa.repository.exceptions.NotFound;
import de.qucosa.repository.exceptions.SaveFailed;
import de.qucosa.repository.model.Url;
import de.qucosa.repository.model.UrlSet;
import de.qucosa.repository.services.UrlService;
import de.qucosa.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping(value = "/url")
public class UrlController extends ControllerAbstract {

    private final UrlService urlService;

    private final RestTemplate restTemplate;

    @Autowired
    public UrlController(UrlService urlService, RestTemplate restTemplate) {
        this.urlService = urlService;
        this.restTemplate = restTemplate;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity createUrl(@RequestBody Url url, HttpServletRequest request) {
        Url output;

        if (url.getLoc() == null || url.getLoc().isEmpty()) {
            return new ErrorDetails(this.getClass().getName(), "createUrl", "POST:url",
                    HttpStatus.BAD_REQUEST, "Requestbody has to contain element loc.", null).response();
        }

        if (url.getUrlSetUri() == null || url.getUrlSetUri().isEmpty()) {
            return new ErrorDetails(this.getClass().getName(), "createUrl", "POST:url",
                    HttpStatus.BAD_REQUEST, "Urlset property in url requestbody failed.", null).response();
        }

        if (url.getLoc().contains("qucosa:")) {
            url.setLoc(url.getLoc().replace("qucosa:", "qucosa%3A"));
        }

        url.setLastmod(Utils.getCurrentW3cDatetime());
        UrlSet urlSet = ensureUrlSet(url.getUrlSetUri(), request);

        if (urlSet.getUri() != null) {
            url.setUrlSetUri(urlSet.getUri());
        }

        url.setLastmod(Utils.getCurrentW3cDatetime());

        try {
            output = urlService.save(url);
        } catch (SaveFailed saveFailed) {
            return new ErrorDetails(this.getClass().getName(), "createUrl", "POST:url/urlset",
                    HttpStatus.NOT_ACCEPTABLE, saveFailed.getMessage(), saveFailed).response();
        }

        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    /* TODO test bulk-delete */
    /* TODO spring security authorization einbauen */
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity delete(@RequestBody Url input) {
        try {
            urlService.deleteUrl(input);
        } catch (DeleteFailed deleteFailed) {
            return new ErrorDetails(this.getClass().getName(), "delete", "DELETE:url",
                    HttpStatus.NOT_ACCEPTABLE, deleteFailed.getMessage(), deleteFailed).response();
        }

        return new ResponseEntity<>(input.getLoc() + " is removed.", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = {"", "/{urlset}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity findUrls(@PathVariable(value = "urlset", required = false) String urlset, @RequestParam(value = "url", required = false) String url) {

        if (url != null && !url.isEmpty()) {
            Url urlData;

            if (url.contains("qucosa:")) {
                url = url.replace("qucosa:", "qucosa%3A");
            }

            try {
                urlData = urlService.findUrl("loc", url);
            } catch (NotFound notFound) {
                return new ErrorDetails(this.getClass().getName(), "findUrls", "GET:url/urlset?url=",
                        HttpStatus.NOT_FOUND, notFound.getMessage(), notFound).response();
            }

            return new ResponseEntity<>(urlData, HttpStatus.FOUND);
        }

        Collection<Url> urlList;

        if (urlset != null && !urlset.isEmpty()) {
            try {
                urlList = urlService.findUrllist("urlset_uri", urlset);
            } catch (NotFound notFound) {
                return new ErrorDetails(this.getClass().getName(), "findUrls", "GET:url/urlset",
                        HttpStatus.NOT_FOUND, notFound.getMessage(), notFound).response();
            }
        } else {
            try {
                urlList = urlService.findAll();
            } catch (NotFound notFound) {
                return new ErrorDetails(this.getClass().getName(), "findUrls", "GET:url",
                        HttpStatus.NOT_FOUND, notFound.getMessage(), notFound).response();
            }
        }

        return new ResponseEntity<>(urlList, HttpStatus.FOUND);
    }

    private UrlSet findUrlSet(String urlset, HttpServletRequest request) {
        return restTemplate.exchange(serverUrl(request, serverPort) + "/urlsets/" + urlset,
                HttpMethod.GET,
                null,
                UrlSet.class).getBody();
    }

    private UrlSet ensureUrlSet(String urlset, HttpServletRequest request) {
        UrlSet urlSet = findUrlSet(urlset, request);

        if (urlSet.getUri() == null) {
            UrlSet newUrlSet = new UrlSet();
            newUrlSet.setUri(urlset);
            newUrlSet.setLastmod(Utils.getCurrentW3cDatetime());
            newUrlSet.setLoc(serverUrl(request, serverPort) + "/urlsets/" + urlset);

            urlSet = restTemplate.postForObject(
                    serverUrl(request, serverPort) + "/urlsets",
                    newUrlSet,
                    UrlSet.class
            );
        }

        return urlSet;
    }
}
