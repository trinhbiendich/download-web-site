package com.sacletest.model;

import org.apache.commons.io.FilenameUtils;

import com.sacletest.service.Config;

public class FileInfo {
	private String filePath;
	private String fileName;
	
	public FileInfo(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}
	
	public FileInfo(String url, Config config) {
		this.fileName = FilenameUtils.getName(url);
		this.filePath = getFilePathFromURL(url, config);
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFilePathFromURL(String url, Config config) {
		String filePath = "";
		// System.out.println("url : " + url);
		String fileName = FilenameUtils.getName(url);
		if (url.startsWith(config.getBaseHost()) && url.length() > config.getBaseHost().length()) {
			filePath = url.substring(config.getBaseHost().length(), url.length());
		}
		// System.out.println("filepath : " + filePath);
		if (filePath.endsWith(fileName))
			filePath = filePath.substring(0, filePath.indexOf(fileName));
		return filePath;
	}
}
