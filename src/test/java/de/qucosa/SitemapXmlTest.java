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

package de.qucosa;

import de.qucosa.model.SitemapIndexModel;
import de.qucosa.model.SitemapModel;
import de.qucosa.model.Url;
import de.qucosa.model.Urlset;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SitemapXmlTest {
    private Url url = new Url("https://example.com/landingpage1", "2018-10-10");
    private Url url2 = new Url("https://example.com/landingpage2", "2018-10-10");
    private Url url3 = new Url("https://example.com/landingpage3", "2018-10-10");

    private Urlset urlset = new Urlset("slub");
    private Urlset urlset2 = new Urlset("ubl");

    @Test
    public void testTenantSitemap() throws JAXBException, IOException {
        SitemapModel sitemap = new SitemapModel();
        sitemap.getUrl().add(url);
        sitemap.getUrl().add(url2);
        sitemap.getUrl().add(url3);

        JAXBContext context = JAXBContext.newInstance(SitemapModel.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(sitemap, new FileWriter("testTenant.xml"));
    }

    @Test
    public void testGlobalSitemap() throws JAXBException, IOException {
        SitemapIndexModel sitemap = new SitemapIndexModel();

        urlset.setUrlList(Arrays.asList(url, url2));
        urlset.setLastmod("2018-10-10");
        urlset2.setUrlList(Arrays.asList(url3));
        sitemap.setUrlset(Arrays.asList(urlset, urlset2));

        JAXBContext context = JAXBContext.newInstance(SitemapIndexModel.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(sitemap, new FileWriter("testGlobal.xml"));
    }
}
