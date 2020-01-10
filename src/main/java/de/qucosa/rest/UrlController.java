package de.qucosa.rest;

import de.qucosa.ErrorDetails;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/url")
public class UrlController extends ControllerAbstract {

    private UrlService urlService;

    private RestTemplate restTemplate;

    @Autowired
    public UrlController(UrlService urlService, RestTemplate restTemplate) {
        this.urlService = urlService;
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = "{urlset}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity createUrl(@PathVariable("urlset") String urlset, @RequestBody Url url,
                                    HttpServletRequest request) {
        Url output = new Url();

        if (url.getLoc().isEmpty()) {
            return new ErrorDetails(this.getClass().getName(), "createUrl", "POST:url",
                    HttpStatus.BAD_REQUEST, "Requestbody has to contain Element 'loc'.", null).response();
        }

        if (url.getLastmod().isEmpty()) {
            url.setLastmod(Utils.getCurrentW3cDatetime());
        }

        UrlSet urlSet = urlSet(urlset, request);

        if (urlSet.getUri() == null) {
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

    private UrlSet urlSet(String urlset, HttpServletRequest request) {
        UrlSet urlSet = restTemplate.exchange(serverUrl(request) + "/urlsets/" + urlset,
                HttpMethod.GET,
                null,
                UrlSet.class).getBody();

        if (urlSet.getUri() == null) {
            UrlSet newUrlSet = new UrlSet();
            newUrlSet.setUri(urlset);
            newUrlSet.setLastmod(Utils.getCurrentW3cDatetime());
            newUrlSet.setLoc(serverUrl(request) + "/urlsets/" + urlset);

            urlSet = restTemplate.postForObject(
                    serverUrl(request) + "/urlsets",
                    newUrlSet,
                    UrlSet.class
            );
        }

        return urlSet;
    }
}
