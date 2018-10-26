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

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name="urlset")
public class SitemapModel {
    List<Url> url = new ArrayList<Url>();

    public List<Url> getUrl() {
        return this.url;
    }

    public void setUrl(List<Url> url) {
        this.url = url;
    }

    List<Urlset> urlset = new ArrayList<>();

    public List<Urlset> getUrlset() {
        return urlset;
    }

    public void setUrlset(List<Urlset> urlset) {
        this.urlset = urlset;
    }
}