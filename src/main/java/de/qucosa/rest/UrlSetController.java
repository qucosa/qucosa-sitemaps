package de.qucosa.rest;

import de.qucosa.ErrorDetails;
import de.qucosa.repository.exceptions.DeleteFailed;
import de.qucosa.repository.exceptions.NotFound;
import de.qucosa.repository.exceptions.SaveFailed;
import de.qucosa.repository.model.UrlSet;
import de.qucosa.repository.services.UrlSetService;
import de.qucosa.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/urlsets")
public class UrlSetController extends ControllerAbstract {
    private UrlSetService urlSetService;

    public UrlSetController(UrlSetService urlSetService) {
        this.urlSetService = urlSetService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity createUrlSet(@RequestBody UrlSet urlset, HttpServletRequest request) {
        UrlSet output = null;

        if (urlset.getUri().isEmpty()) {
            return new ErrorDetails(this.getClass().getName(), "createUrlSet", "POST:urlsets",
                    HttpStatus.BAD_REQUEST, "UrlSet uri ist empty.", null).response();
        }

        urlset.setLoc(serverUrl(request, serverPort)+ "/urlsets/" + urlset.getUri());
        urlset.setLastmod(Utils.getCurrentW3cDatetime());

        try {
            output = urlSetService.saveUrlSet(urlset);
        } catch (SaveFailed saveFailed) {
            return new ErrorDetails(this.getClass().getName(), "createUrlSet", "POST:urlsets",
                    HttpStatus.ALREADY_REPORTED, saveFailed.getMessage(), saveFailed).response();
        }

        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "{urlset}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity deleteUrlSet(@PathVariable("urlset") String urlset) {

        try {
            urlSetService.deleteUrlSet("uri", urlset);
        } catch (DeleteFailed deleteFailed) {
            return new ErrorDetails(this.getClass().getName(), "deleteUrlSet", "DELETE:urlsets/urlset",
                    HttpStatus.NOT_ACCEPTABLE, deleteFailed.getMessage(), deleteFailed).response();
        }

        return new ResponseEntity<>("Urlset / Tenant " + urlset + " is deleted.", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "{urlset}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity find(@PathVariable("urlset") String urlset, HttpServletRequest request) {
        serverUrl(request, serverPort);
        UrlSet urlSet = new UrlSet();

        try {
            urlSet = urlSetService.findByUri("uri", urlset);
        } catch (NotFound notFound) {
            return new ErrorDetails(this.getClass().getName(), "find", "GET:urlsets/urlset",
                    HttpStatus.NOT_FOUND, notFound.getMessage(), notFound).response();
        }

        return new ResponseEntity<>(urlSet, HttpStatus.FOUND);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity findAll() {
        Collection<UrlSet> urlSets = new ArrayList<>();

        try {
            urlSets = urlSetService.findAll();
        } catch (NotFound notFound) {
            return new ErrorDetails(this.getClass().getName(), "findAll", "GET:urlsets",
                    HttpStatus.NOT_FOUND, notFound.getMessage(), notFound).response();
        }

        return new ResponseEntity<>(urlSets, HttpStatus.FOUND);
    }
}
