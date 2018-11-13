package com.sacle.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sacle.common.Constants;
import com.sacle.service.Config;

public class ConfigImpl implements Config {
	
	private String baseHost;
	private String folderSave;
	private String siteName;
	private String siteUrl;
	private Boolean overrideFile;
	private String hostToFind;
	private String hostToReplace;
	
	
	public ConfigImpl(){
		baseHost = null;
		folderSave = null;
		siteName = null;
		
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(Constants.CONFIG_FILE);
			// load a properties file
			prop.load(input);
			//Redis
			this.baseHost = prop.getProperty(Constants.BASE_HOST);
			this.folderSave = prop.getProperty(Constants.FOLDER_TO_SAVE);
			this.siteName = prop.getProperty(Constants.SITE_NAME);
			this.siteUrl = prop.getProperty(Constants.SITE_URL);
			this.overrideFile = Boolean.parseBoolean(prop.getProperty(Constants.OVERRIDE_FILE));
			this.hostToFind = prop.getProperty(Constants.HOST_TO_FIND);
			this.hostToReplace = prop.getProperty(Constants.HOST_TO_REPLACE);
			
		} catch (IOException ex) {
			System.out.println("Could not read property : " + ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println("Could not close file : " + e.getMessage());
				}
			}
		}
		System.out.println("Config info : \n " + showInfo());
	}
	
	@Override
	public String getBaseHost() {
		return baseHost;
	}
	
	@Override
	public String getFolderToSave() {
		return folderSave;
	}
	
	@Override
	public String getSiteName() {
		return siteName;
	}
	
	@Override
	public String getSite() {
		return siteUrl;
	}
	
	@Override
	public boolean getOverrideFile() {
		return overrideFile;
	}
	
	@Override
	public String getHostToFind() {
		return hostToFind;
	}
	
	@Override
	public String getHostToReplace() {
		return hostToReplace;
	}
	
	@Override
	public File getRootFolder() {
		File rootFolder = new File(folderSave);
		if(!rootFolder.exists()){
			rootFolder.mkdirs();
		}
		return rootFolder;
	}

	@Override
	public String showInfo() {
		return "\n========================================================"
				+ "\nInfo : \n"
				+ "\n Base host : " + getBaseHost()
				+ "\n Folder to save : " + getFolderToSave()
				+ "\n Site name : " + getSiteName()
				+ "\n Site to download : " + getSite()
				+ "\n========================================================\n\n";
	}
}
