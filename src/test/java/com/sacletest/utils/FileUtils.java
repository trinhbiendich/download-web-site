package com.sacletest.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.sacletest.MyTest;
import com.sacletest.model.FileInfo;
import com.sacletest.service.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	private static Logger log = LoggerFactory.getLogger(FileUtils.class);

	public static File getFolderToSaveFile(String filePath, Config config){
		File folderToSave = new File(config.getRootFolder(), filePath);
		if (!folderToSave.exists()) {
			folderToSave.mkdirs();
		}
		return folderToSave;
	}
	
	public static void saveFileFromString(String filePath, String fileName, String content, Config config) {
		File folderToSave = getFolderToSaveFile(filePath, config);
		File file = new File(folderToSave, fileName);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter BW = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
			BW.write(content);
			BW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void saveFileFromUrl(String url, Config config){
		FileInfo fileInfo = new FileInfo(url, config);
		File folderToSave = getFolderToSaveFile(fileInfo.getFilePath(), config);
		File file = new File(folderToSave, fileInfo.getFileName());
		if(file.exists() && !config.getOverrideFile()){
			log.debug("File [{}] is downloaded", fileInfo.getFileName());
			return;
		}
		
		byte[] fileContent = null;
		if(url.startsWith("https")){
			fileContent = ContentUtils.urlHttpsToBytes(url);
		}else{
			fileContent = ContentUtils.urlToBytes(url);
		}
		
        if (fileContent != null) {
            try (FileOutputStream fos = new FileOutputStream(file);) {
                fos.write(fileContent);
			} catch (Exception ex) {
                log.error("Can't save file {}", ex.getMessage());
            }
        }
	}
}
