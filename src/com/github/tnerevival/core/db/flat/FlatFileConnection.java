package com.github.tnerevival.core.db.flat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author Daniel Vidmar aka creatorfromhell
 *
 */
public class FlatFileConnection {
	
	private String fileName;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public FlatFileConnection(String fileName) {
		this.fileName = fileName;
	}
	
	public void connect() throws FileNotFoundException, IOException {
		ois = new ObjectInputStream(new FileInputStream(fileName));
		oos = new ObjectOutputStream(new FileOutputStream(fileName));
	}
	
	public Boolean connected() {
		return ois != null && oos != null;
	}
	
	public void close() {
		try {
			ois.close();
			oos.flush();
			oos.close();
			ois = null;
			oos = null;
		} catch (IOException e) {
			System.out.println("There was an error closing the Object Streams.");
			e.printStackTrace();
		}
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the ois
	 */
	public ObjectInputStream getOIS() {
		return ois;
	}

	/**
	 * @param ois the ois to set
	 */
	public void setOIS(ObjectInputStream ois) {
		this.ois = ois;
	}

	/**
	 * @return the oos
	 */
	public ObjectOutputStream getOOS() {
		return oos;
	}

	/**
	 * @param oos the oos to set
	 */
	public void setOOS(ObjectOutputStream oos) {
		this.oos = oos;
	}
}