package org.sc.filemonitor.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigurationBuilder {

	private Path                  path    = null;
	private WatchEvent.Kind<?> [] events  = null;
	private String             [] scripts = null;
	
	private ConfigurationBuilder() {}
	
	public static ConfigurationBuilder get() {
		return new ConfigurationBuilder();
	}
	
	public static ConfigurationBuilder from( Path path ) {
		
		ConfigurationBuilder builder    = get();
		
		try ( InputStream input = Files.newInputStream( path ) ) {
			
			Properties properties = new Properties();
			properties.load( input );
			
			builder.path( Paths.get( properties.getProperty( "path" ) ) )
				   .events( toEvents( properties.getProperty( "events" ) ) )
				   .scripts( toScripts( properties.getProperty( "scripts" ) ) );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return builder;
	}
	
	public ConfigurationBuilder path( Path path ) {
		this.path = path;
		return this;
	}
	
	public ConfigurationBuilder events( WatchEvent.Kind<?> ... events ) {
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
	
	private static WatchEvent.Kind<?> [] toEvents( String property ) {
		
		String []                parts  = property.split( "," );
		List<WatchEvent.Kind<?>> events = new ArrayList<>();
		
		for ( String part : parts ) {
			if ( "create".equals( part ) ) events.add( StandardWatchEventKinds.ENTRY_CREATE );
			if ( "delete".equals( part ) ) events.add( StandardWatchEventKinds.ENTRY_DELETE );
			if ( "modify".equals( part ) ) events.add( StandardWatchEventKinds.ENTRY_MODIFY );
		}
		
		return events.toArray( new  WatchEvent.Kind<?> [events.size()] );
		
	}
	
	private static String [] toScripts( String property ) {
		if ( property != null ) return property.split( "," );
		return new String [] {};
	}
	
}
