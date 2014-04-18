package com.github.tnerevival.core.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.tnerevival.core.db.flat.FlatFileConnection;

/**
 * 
 * @author Daniel Vidmar aka creatorfromhell
 *
 */
public class FlatFile extends Database {
	
	private String file;
	private FlatFileConnection connection;
	
	public FlatFile(String file) {
		this.file = file;
		connection = new FlatFileConnection(file);
	}

	@Override
	public Boolean connected() {
		if(connection == null) {
			connect();
		}
		return connection.connected();
	}

	@Override
	public void connect() {
		try {
			if(connection == null) {
				connection = new FlatFileConnection(file);
			}
			connection.connect();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file! File:" + file);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object connection() {
		if(connection == null || !connected()) {
			connect();
		}
		return connection;
	}

	@Override
	public void close() {
		connection.close();
	}
	
	public File getFile() {
		return new File(this.file);
	}
}