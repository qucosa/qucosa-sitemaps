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

@RestController
@RequestMapping("/urlsets")
public class UrlSetController {
    private UrlSetService urlSetService;

    public UrlSetController(UrlSetService urlSetService) {
        this.urlSetService = urlSetService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity createUrlSet(@RequestBody UrlSet urlset, HttpServletRequest request) {
        UrlSet output = null;
        String hosturl = request.getRequestURL().toString();

        if (urlset.getUri().isEmpty()) {
            return new ErrorDetails(this.getClass().getName(), "createUrlSet", "POST:urlsets",
                    HttpStatus.BAD_REQUEST, "UrlSet uri ist empty.", null).response();
        }

        urlset.setLoc(hosturl + "/" + urlset.getUri());
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
            urlSetService.deleteUrlSet(urlset);
        } catch (DeleteFailed deleteFailed) {
            return new ErrorDetails(this.getClass().getName(), "deleteUrlSet", "DELETE:urlsets/urlset",
                    HttpStatus.NOT_ACCEPTABLE, deleteFailed.getMessage(), deleteFailed).response();
        }

        return new ResponseEntity<>("Urlset / Tenant '" + urlset + "' is deleted.", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "{urlset}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity find(@PathVariable("urlset") String urlset) {
        UrlSet urlSet = new UrlSet();

        try {
            urlSet = urlSetService.findByUri("uri", urlset);
        } catch (NotFound notFound) {
            return new ErrorDetails(this.getClass().getName(), "find", "GET:urlsets/urlset",
                    HttpStatus.NOT_FOUND, notFound.getMessage(), notFound).response();
        }

        return null;
    }
}
