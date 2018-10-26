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

package de.qucosa.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class Urlset implements Serializable {
    @Id
    @Column(name="uri")
    private String uri;

    private String loc;

    private String lastmod;

    /**
     * parameter orphanRemoval to cascade the remove operation on both entities "Url" and "Urlset",
     * otherwise deleting an Url from both repositories in the Spring Boot SitemapRestController doesn't work.
     *
     * parameter fetch set to "Eager" to handout url-list (i.e. for the sitemap)
     * which otherwise can't be loaded in "Lazy"-fetchtype
     */
    @OneToMany(mappedBy = "urlset", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
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
        return urlList;
    }

    public void addUrl(Url url) {
        this.urlList.add(url);
    }

    public void removeUrl(Url url) {
        this.urlList.remove(url);
    }

    public void setUrlList(List<Url> urlList) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Urlset urlset = (Urlset) o;
        return Objects.equals(uri, urlset.uri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uri);
    }
}
