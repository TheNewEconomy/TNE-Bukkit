package com.github.tnerevival.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.github.tnerevival.TNE;

public class UpdateChecker {
	String[] buildArray;
	
	public UpdateChecker() {
		this.buildArray = getCurrentBuild().split("\\.");
	}
	
	public String getCurrentBuild() {
		String build = TNE.instance.getDescription().getVersion();
		try {
		    URL url = new URL("https://creatorfromhell.com/tne/tnebuild.txt");
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    build = in.readLine();
		    in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return build;
	}
	
	public boolean latest() {
		String build = TNE.instance.getDescription().getVersion();
		String[] currentBuild = build.split("\\.");
		if(Integer.valueOf(this.buildArray[0]) > Integer.valueOf(currentBuild[0])) return false;
		if(Integer.valueOf(this.buildArray[1]) > Integer.valueOf(currentBuild[1])) return false;
		if(Integer.valueOf(this.buildArray[2]) > Integer.valueOf(currentBuild[2])) return false;
		if(Integer.valueOf(this.buildArray[3]) > Integer.valueOf(currentBuild[3])) return false;
		return true;
	}
}