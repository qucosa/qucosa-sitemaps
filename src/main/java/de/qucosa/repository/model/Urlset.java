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

package de.qucosa.repository.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Urlset implements Serializable {
    private String uri;

    private String loc;

    private String lastmod;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Url> urlList;

    public Urlset(String uri) {
        this.uri = uri;
    }

    public Urlset() { super(); }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @XmlTransient
    public String getUri() {
        return uri;
    }

    @XmlTransient
    public List<Url> getUrlList() {
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        return urlList;
    }

    public void addUrl(Url url) {
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        this.urlList.add(url);
    }

    public void removeUrl(Url url) {
        this.urlList.remove(url);
    }

    public void setUrlList(List<Url> urlList) {
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        this.urlList = urlList;
    }

    @XmlElement
    public String getLastmod() {
        return lastmod;
    }

    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }

    @XmlElement
    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
