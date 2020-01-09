package de.qucosa.rest;

import de.qucosa.ErrorDetails;
import de.qucosa.repository.exceptions.SaveFailed;
import de.qucosa.repository.model.UrlSet;
import de.qucosa.repository.services.UrlSetService;
import de.qucosa.utils.Utils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private Environment environment;

    public UrlSetController(UrlSetService urlSetService, Environment environment) {
        this.urlSetService = urlSetService;
        this.environment = environment;
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
}
