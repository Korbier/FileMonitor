package org.sc.filemonitor.configuration;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public class Configuration {

	private Path                  path    = null;
	private WatchEvent.Kind<?> [] events  = null;
	private String             [] scripts = null;
	
	Configuration( ConfigurationBuilder builder ) {
		this.path    = builder.getPath();
		this.events  = builder.getEvents();
		this.scripts = builder.getScripts();
	}
	
	public Path getPath() {
		return path;
	}
	
	public WatchEvent.Kind<?>[] getEvents() {
		return events;
	}
	
	public String [] getScripts() {
		return scripts;
	}
}
