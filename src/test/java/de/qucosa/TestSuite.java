package de.qucosa;

import de.qucosa.rest.SitemapControllerIT;
import de.qucosa.rest.UrlControllerIT;
import de.qucosa.rest.UrlSetControllerIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@SuppressWarnings("JUnit5Platform")
@RunWith(JUnitPlatform.class)
@DisplayName("All sitemap controller ITs.")
@SelectClasses({UrlSetControllerIT.class, UrlControllerIT.class, SitemapControllerIT.class})
public class TestSuite {
}
