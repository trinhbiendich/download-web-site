package com.sacle.service;

import java.io.File;

public interface Config {
	String getBaseHost();
	String getFolderToSave();
	String getSiteName();
	String getSite();
	
	File getRootFolder();
	String showInfo();
}
