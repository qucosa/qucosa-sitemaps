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



import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Urlset {
    @Id
    @GeneratedValue
    @Column(name="id")
    private long id;
    private String uri;

    @OneToMany(mappedBy = "urlset", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Url> urlList;

    public Urlset(String uri) {
        this.uri = uri;
    }

    public Urlset() { super(); }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

//    @OneToMany
//    @JsonManagedReference
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

}
