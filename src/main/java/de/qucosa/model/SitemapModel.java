/*
 * Copyright 2017 Saxon State and University Library Dresden (SLUB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package de.qucosa.model;

import de.qucosa.repository.model.Url;
import de.qucosa.repository.model.UrlSet;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@XmlRootElement(name="urlset")
public class SitemapModel {
    private Collection<Url> url = new ArrayList<>();

    public Collection<Url> getUrl() {
        return url;
    }

    public void setUrl(Collection<Url> url) {
        this.url = url;
    }

    private List<UrlSet> urlset = new ArrayList<>();

    public List<UrlSet> getUrlset() {
        return urlset;
    }

    public void setUrlset(List<UrlSet> urlset) {
        this.urlset = urlset;
    }
}