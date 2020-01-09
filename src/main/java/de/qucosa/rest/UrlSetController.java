package de.qucosa.rest;

import de.qucosa.ErrorDetails;
import de.qucosa.repository.exceptions.SaveFailed;
import de.qucosa.repository.model.UrlSet;
import de.qucosa.repository.services.UrlSetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/urlsets")
public class UrlSetController {
    private UrlSetService urlSetService;

    public UrlSetController(UrlSetService urlSetService) {
        this.urlSetService = urlSetService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity createUrlSet(@RequestBody UrlSet urlset) {
        UrlSet output = null;

        try {
            output = urlSetService.saveUrlSet(urlset);
        } catch (SaveFailed saveFailed) {
            return new ErrorDetails(this.getClass().getName(), "createUrlSet", "POST:urlsets",
                    HttpStatus.ALREADY_REPORTED, saveFailed.getMessage(), saveFailed).response();
        }

        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }
}
