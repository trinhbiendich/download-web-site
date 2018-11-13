package com.sacle.service;

import java.io.File;

public interface Config {
	String getBaseHost();
	String getFolderToSave();
	String getSiteName();
	String getSite();
	boolean getOverrideFile();
	String getHostToFind();
	String getHostToReplace();
	
	File getRootFolder();
	String showInfo();
}
