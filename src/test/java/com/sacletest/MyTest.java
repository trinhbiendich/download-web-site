package com.sacletest;

import com.sacletest.model.FileInfo;
import com.sacletest.service.Config;
import com.sacletest.service.impl.ConfigImpl;
import com.sacletest.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyTest {
    public static WebDriver driver;
    public static Config config = null;
    public static List<String> processedLinks;
    private static Logger log = LoggerFactory.getLogger(MyTest.class);

    @BeforeClass
    public static void beforeClass() {
        File file = new File("drivers/geckodriver.exe");

        Assert.assertTrue(file.exists());

        System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
        driver = new FirefoxDriver();
        config = new ConfigImpl();
        processedLinks = Collections.synchronizedList(new ArrayList<>());
    }

    @AfterClass
    public static void afterClass() {
        driver.quit();
    }

    @Test
    public void open() {
        List<String> links = new ArrayList<>();
        links.add(config.getSite());
        while (!links.isEmpty()) {
            String url = links.get(0);
            links.remove(url);
            if (processedLinks.contains(url)) {
                continue;
            }
            links.addAll(downloadHtmlbyUrl(url));
            processedLinks.add(url);
        }

    }

    public List<String> downloadHtmlbyUrl(String url) {
        List<String> links = new ArrayList<>();
        log.debug("|=========================================================================|");
        log.debug("Process for quere [" + links.size() + "]: " + url);
        if (StringUtils.isBlank(url) || processedLinks.contains(url)) {
            return links;
        }

        FileInfo fileInfo = new FileInfo(url, config);

        //Open url
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        log.debug("Path : " + fileInfo.getFilePath());

        /**
         * xu ly url sau do add vao queue
         * */
        links = processUrl();

        processFile("link", "href");
        processFile("script", "src");
        processFile("img", "src");

        //save html
        FileUtils.saveFileFromUrl(url, config);
        //FileUtils.saveFileFromString(fileInfo.getFilePath(), fileInfo.getFileName(), driver.getPageSource(), config);
        return links;
    }

    public void processFile(String tag, String attr) {
        List<WebElement> linkTags = driver.findElements(By.tagName(tag));
        for (WebElement element : linkTags) {
            String link = null;
            try {
                link = element.getAttribute(attr);
            } catch (Exception ex) {
                log.error("can't get attribute {} error {}", element, ex);
            }

            if (StringUtils.isBlank(link) || !link.startsWith(config.getBaseHost())) {
                continue;
            }
            if (link.contains("#")) {
                link = link.substring(0, link.indexOf("#"));
            }
            if (link.contains("?")) {
                link = link.substring(0, link.indexOf("?"));
            }

            FileUtils.saveFileFromUrl(link, config);
        }
    }

    public List<String> processUrl() {
        List<WebElement> linkA = driver.findElements(By.tagName("a"));
        List<String> links = new ArrayList<>();
        int total = 0;
        for (WebElement element : linkA) {
            if (element == null)
                continue;
            String href = null;
            try {
                href = element.getAttribute("href").trim();
            } catch (Exception ex) {
                System.out.println("err: " + ex.getMessage());
            }
            if (StringUtils.isBlank(href)) {
                continue;
            }
            if (href.equals("#") || href.startsWith("//") || !href.startsWith(config.getBaseHost()))
                continue;
            if (href.contains("#")) {
                href = href.substring(0, href.indexOf("#"));
            }
            if (href.contains("?")) {
                href = href.substring(0, href.indexOf("?"));
            }
            if (processedLinks.contains(href) || links.contains(href))
                continue;
            links.add(href);
            log.debug(href);
            total++;
        }
        log.debug("Added " + total + " to queue");
        return links;
    }

}
