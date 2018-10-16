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

import de.qucosa.model.Url;
import de.qucosa.model.Urlset;
import de.qucosa.repository.UrlRepository;
import de.qucosa.repository.UrlSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class SitemapRestController {

    @Autowired
    private UrlSetRepository urlSetRepository;
    @Autowired
    private UrlRepository urlRepository;

    /**
     * url-operation to
     * create url in given urlSetName
     */
    @RequestMapping(method = POST, value = "/urlsets/{urlSetName}")
    @ResponseBody
    public ResponseEntity createUrl(@PathVariable("urlSetName") String urlSetName, @RequestBody Url url) {
        List<Urlset> urlsets = urlSetRepository.findAll();

        for (Urlset set : urlsets) {
            if (set.getUri().equals(urlSetName)) {
                urlSetRepository.findById(set.getId()).map(urlset -> {
                    url.setUrlset(urlset);
                    return urlRepository.save(url);
                });

                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }

        return new ResponseEntity<>("urlSet '" + urlSetName + "' not found.", HttpStatus.NOT_FOUND);
    }

    // TODO: urls als eingabeparameter
    /**
     * url-operation to
     * delete url in given urlSetName
     */
    @RequestMapping(method = DELETE, value = "/urlsets/{urlSetName}/{url}")
    @ResponseBody
    public ResponseEntity deleteUrl(@PathVariable("urlSetName") String urlSetName, @PathVariable("url") String url) {
        List<Urlset> urlsets = urlSetRepository.findAll();

        for (Urlset set : urlsets) {
            if (set.getUri().equals(urlSetName)) {
                urlSetRepository.findById(set.getId()).map(urlset -> {
                    List<Url> urlList = urlRepository.findAll();
                    for (Url urlEntry : urlList) {
                        if (urlEntry.getLoc().equals(url)) {
                            urlRepository.findById(urlEntry.getId()).map(url2 -> {
                                urlRepository.delete(urlEntry);

                                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                            });
                            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                        }
                    }
                    urlSetRepository.save(set);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                });
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * urlset-operation to
     * create urlSets
     */
    @RequestMapping(method = POST, value = "/urlsets")
    @ResponseBody
    public ResponseEntity createUrlSet(@RequestBody Urlset urlset) {
        urlSetRepository.save(urlset);

        return new ResponseEntity<>(urlset, HttpStatus.CREATED);
    }

    /**
     * urlSet-operation to
     * get all urlSets
     */
    @RequestMapping(method = GET, value = "/urlsets")
    @ResponseBody
    public ResponseEntity getAllUrlSets() {
        List<Urlset> allUrlsets = urlSetRepository.findAll();

        return new ResponseEntity<>(allUrlsets, HttpStatus.OK);
    }

    /**
     * urlSet-operation to
     * get all urlSets
     */
    @RequestMapping(method = GET, value = "/urlsets/{urlSetName}")
    @ResponseBody
    public ResponseEntity getUrlSet(@PathVariable("urlSetName") String urlSetName) {
        List<Urlset> urlsets = urlSetRepository.findAll();

        for (Urlset set : urlsets) {
            if (set.getUri().equals(urlSetName)) {
                return new ResponseEntity<>(set, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * urlSet-operation to
     * delete specific urlset, identified by setname
     */
    @RequestMapping(method = DELETE, value = "/urlsets/{urlSetName}")
    @ResponseBody
    public ResponseEntity deleteUrlSet(@PathVariable("urlSetName") String urlSetName) {
        List<Urlset> urlsets = urlSetRepository.findAll();

        for (Urlset set : urlsets) {
            if (set.getUri().equals(urlSetName)) {
                urlSetRepository.delete(set);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
