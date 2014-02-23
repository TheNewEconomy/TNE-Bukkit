package com.github.tnerevival.core.db.flat;

import java.io.Serializable;
import java.util.HashMap;

public class Article implements Serializable {

	private static final long serialVersionUID = 1L;
	HashMap<String, Entry> entries = new HashMap<String, Entry>();
	
	String name;
	
	public Article(String name) {
		this.name = name;
	}
	
	public void addEntry(Entry entry) {
		entries.put(entry.getName(), entry);
	}
	
	public Entry getEntry(String identifier) {
		return entries.get(identifier);
	}
	
	public HashMap<String, Entry> getEntries() {
		return entries;
	}
}