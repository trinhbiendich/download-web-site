package com.sacletest.service;

import java.io.File;

public interface Config {
	String getBaseHost();
	String getFolderToSave();
	String getSiteName();
	String getSite();
	boolean getOverrideFile();
	
	File getRootFolder();
	String showInfo();
}
