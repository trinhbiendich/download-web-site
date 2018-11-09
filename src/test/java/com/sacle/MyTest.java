package com.sacle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.sacle.model.FileInfo;
import com.sacle.service.Config;
import com.sacle.service.impl.ConfigImpl;
import com.sacle.utils.FileUtils;

public class MyTest {
	public static WebDriver driver;
	public static Config config = null;
	public static List<String> links;
	public static List<String> processedLinks;
	
	@BeforeClass
	public static void beforeClass(){
		System.setProperty("webdriver.gecko.driver", "/media/DATA/000-Home/workspaces/workspace-aviva-uk/download-web-page/drivers/geckodriver");
		driver = new FirefoxDriver();
		config = new ConfigImpl();
		links = Collections.synchronizedList(new ArrayList<>());
		processedLinks = Collections.synchronizedList(new ArrayList<>());
	}
	
	@AfterClass
	public static void afterClass() {
		driver.quit();
	}
	
	@Test
	public void open() {
		
		downloadHtmlbyUrl(config.getSite());
	}
	
	public synchronized void downloadHtmlbyUrl(String url) {
		System.out.println("\n\n");
		System.out.println("|=========================================================================|");
		System.out.println("Process for quere [" + links.size() + "]: " + url);
		if(StringUtils.isBlank(url) || processedLinks.contains(url)){
			nextUrl();
			return;
		}
		
		processedLink(url);
		
		FileInfo fileInfo = new FileInfo(url, config);
		
		//Open url
		driver.get(url);
		
		System.out.println("Path : " + fileInfo.getFilePath());
		
		/**
		 * xu ly url sau do add vao queue
		 * */
		processUrl();
		
		processFile("link", "href");
		processFile("script", "src");
		processFile("img", "src");
		
		//save html
		FileUtils.saveFileFromString(fileInfo.getFilePath(), fileInfo.getFileName(), driver.getPageSource(), config);
		
		nextUrl();
	}
	
	public synchronized void processFile(String tag, String attr) {
		List<WebElement> linkTags = driver.findElements(By.tagName(tag));
		for(WebElement element : linkTags){
			String link = element.getAttribute(attr);
			if(StringUtils.isBlank(link) || !link.startsWith(config.getBaseHost()))
				continue;
			if(link.contains("#"))
				link = link.substring(0, link.indexOf("#"));
			if(link.contains("?"))
				link = link.substring(0, link.indexOf("?"));
			
			
			FileUtils.saveFileFromUrl(link, config);
		}
	}

	public synchronized void processedLink(String url){
		if(!processedLinks.contains(url))
			processedLinks.add(url);
		if(links.contains(url))
			links.remove(url);
	}
	
	public synchronized void nextUrl(){
		if(links.size() > 0)
			downloadHtmlbyUrl(links.get(0));
	}
	
	public synchronized void processUrl(){
		List<WebElement> linkA = driver.findElements(By.tagName("a"));
		int total = 0;
		for(WebElement element : linkA){
			if(element == null || StringUtils.isBlank(element.getAttribute("href")))
				continue;
			String href = element.getAttribute("href").trim();
			if(href.equals("#") || href.startsWith("//") || !href.startsWith(config.getBaseHost()))
				continue;
			if(href.contains("#")){
				href = href.substring(0, href.indexOf("#"));
			}
			if(href.contains("?")){
				href = href.substring(0, href.indexOf("?"));
			}
			if(processedLinks.contains(href) || links.contains(href))
				continue;
			links.add(href);
			System.out.println(href);
			total++;
		}
		System.out.println("Added " + total + " to queue");
	}
	
}
