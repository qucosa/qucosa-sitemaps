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

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

public class Url implements Serializable {
    private UrlSet urlset;

    private String loc;

    private String lastmod;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String changefreq;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String priority;

    public Url(String loc, String lastmod) {
        this.loc = loc;
        this.lastmod = lastmod;
    }

    public Url(String loc, String lastmod, String changefreq, String priority) {
        this.loc = loc;
        this.lastmod = lastmod;
        this.changefreq = changefreq;
        this.priority = priority;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLastmod() {
        return lastmod;
    }

    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }

    public String getChangefreq() {
        return changefreq;
    }

    public void setChangefreq(String changefreq) {
        this.changefreq = changefreq;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @XmlTransient
    public UrlSet getUrlset() { return urlset; }

    public void setUrlset(UrlSet urlset) { this.urlset = urlset; }
}
