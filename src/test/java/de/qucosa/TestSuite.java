package de.qucosa;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@SuppressWarnings("JUnit5Platform")
@RunWith(JUnitPlatform.class)
@DisplayName("All sitemap controller ITs.")
@SelectPackages("de.qucosa.rest")
//@SelectClasses({UrlSetControllerIT.class,
//        UrlControllerIT.class,
//        SitemapControllerIT.class})
public class TestSuite {
}
