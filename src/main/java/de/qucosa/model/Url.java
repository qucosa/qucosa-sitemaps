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


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Url {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="urlset_id")
    @JsonBackReference
//    @JsonIgnore
    private Urlset urlset;
    private String loc;
    private String lastmod;
    private String changefreq;
    private String priority;

    public Url() {
        super();
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

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

//    @ManyToOne
//    @JoinColumn(name="", nullable=false)
//    @JsonBackReference
    public Urlset getUrlset() { return urlset; }

    public void setUrlset(Urlset urlset) { this.urlset = urlset; }
}
