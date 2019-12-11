package org.sc.filemonitor.configuration;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public class ConfigurationBuilder {

	private Path                  path    = null;
	private WatchEvent.Kind<?> [] events  = null;
	private String             [] scripts = null;
	
	private ConfigurationBuilder() {}
	
	public static ConfigurationBuilder get() {
		return new ConfigurationBuilder();
	}
	
	public ConfigurationBuilder path( Path path ) {
		this.path = path;
		return this;
	}
	
	public ConfigurationBuilder events(  WatchEvent.Kind<?> ... events ) {
		this.events = events;
		return this;
	}
	
	public ConfigurationBuilder scripts( String ... scripts ) {
		this.scripts = scripts;
		return this;
	}
	
	public Configuration build() {
		return new Configuration( this );
	}
	
	Path getPath() {
		return path;
	}
	
	WatchEvent.Kind<?>[] getEvents() {
		return events;
	}
	
	String[] getScripts() {
		return scripts;
	}
	
}
