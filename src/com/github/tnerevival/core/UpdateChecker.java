package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.version.ReleaseType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {
	private String build;
	
	public UpdateChecker() {
		build = getLatestBuild();
	}
	
	public String getLatestBuild() {
		String build = TNE.instance.getDescription().getVersion();
		try {
		    URL url = new URL("https://creatorfromhell.com/tne/tnebuild.txt");
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    build = in.readLine();
		    in.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return build;
	}

	public String getCurrentBuild() {
	  return TNE.instance.getDescription().getVersion();
  }

	public ReleaseType getRelease() {
    Integer latest = Integer.valueOf(build.replace(".", ""));
    Integer current = Integer.valueOf(getCurrentBuild().replace(".", ""));

    if(latest < current) return ReleaseType.PRERELEASE;
    if(latest == current) return ReleaseType.LATEST;
    return ReleaseType.OUTDATED;
  }
}