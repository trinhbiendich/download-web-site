package com.sacle;

import java.io.File;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.sacle.service.Config;
import com.sacle.service.impl.ConfigImpl;
import com.sacle.model.FileInfo;

//(?:(?:https?|ftp):\/\/)?[\w/\-?=%.]+\.[\w/\-?=%.]+

public class ValidData {
	private static String regex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	private static String source = "";
	
	public static void main(String[] args) {
		Config config = new ConfigImpl();
		
		
		
		
		try{
			regex = FileUtils.readFileToString(new File("regex.txt"), Charset.forName("UTF-8"));
			source = FileUtils.readFileToString(new File("error.txt"), Charset.forName("UTF-8"));
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		if(StringUtils.isBlank(regex)){
			System.out.println("Regex not ok");
			return;
		}
		System.out.println("Regex : " + regex);
		
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		Matcher m1 = p.matcher(source.replace("\n", "").replace("\r", ""));
		
		
		while(m1.find()){
			String nextUrl = m1.group(0);
			if(StringUtils.isBlank(nextUrl))
				continue;
			nextUrl = getValidUrl(nextUrl, config);
			
			FileInfo fileInfo = new FileInfo(nextUrl, config);
			
			System.out.println("Path : " + fileInfo.getFilePath());
			
			com.sacle.utils.FileUtils.saveFileFromUrl(nextUrl, config);
			
		}
		System.out.println("Done");
	}
	
	public static String getValidUrl(String url, Config config){
		return url.replaceAll(config.getHostToFind(), config.getHostToReplace());
	}
}