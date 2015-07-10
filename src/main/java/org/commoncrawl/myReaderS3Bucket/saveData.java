package org.commoncrawl.myReaderS3Bucket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class saveData {

	public void createFolder(String path, String filename) {
		try {
			File f = new File(path + filename);
			f.delete();
			f.getParentFile().mkdirs();
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeInFile(String path, String filename, String url) {
		FileWriter fstream;
		try {
			fstream = new FileWriter(path + filename, true);
			BufferedWriter fbw = new BufferedWriter(fstream);
			fbw.write(url);
			fbw.newLine();
			fbw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
