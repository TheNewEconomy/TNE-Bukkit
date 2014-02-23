package com.github.tnerevival.core.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.db.flat.Section;

public class TNEDB {

	HashMap<String, Section> sections = new HashMap<String, Section>();
	
	String fileName;
	Integer fileVersion;
	Integer currentVersion;
	
	public TNEDB(Integer currentVersion) {
		this.currentVersion = currentVersion;
		this.fileName = TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.FlatFile.File");
	}
	
	public void initiate() {
		if(firstRun()) {
			create();
		} else {
			getVersion();
		}
	}
	
	private void create() {
		File file = new File(fileName);
		try {
			TNE.instance.getDataFolder().mkdir();
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Boolean firstRun() {
		File file = new File(fileName);
		return file.exists();
	}
	
	private void getVersion() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			fileVersion = ois.readInt();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}