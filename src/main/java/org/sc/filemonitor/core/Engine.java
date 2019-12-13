package org.sc.filemonitor.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.sc.filemonitor.configuration.Configuration;
import org.sc.filemonitor.configuration.ConfigurationBuilder;
import org.sc.filemonitor.core.script.ScriptContext;
import org.sc.filemonitor.core.script.ScriptManager;

public class Engine {

	public final static String PATH_CONFIGURATION = "data/conf/enabled";
	
	private Map<String, Monitor> monitors      = new HashMap<>();
	private ScriptContext        context       = null;
	private ScriptManager        scriptManager = null;
	
	public Engine() {
		this.context       = new ScriptContext( this );
		this.scriptManager = new ScriptManager( this.context ); 
	}
	
	public void start() {		
		loadConfiguration();
		monitorConfigurationDirectory();
		startMonitors();		
	}
	
	private void loadConfiguration() {
		
		try {
			Files.list( Paths.get( PATH_CONFIGURATION ) ).forEach( path -> {
				this.monitor( path.toString(), ConfigurationBuilder.from( path ).build() );
			});
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void monitorConfigurationDirectory() {
		
		Configuration configuration = ConfigurationBuilder.get()
			  .path( Paths.get( PATH_CONFIGURATION ) )
			  .events( StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE )
			  .scripts( "java:org.sc.filemonitor.configuration.ConfigurationLoaderScript" )
			  .build();
		
		this.monitor( configuration );
		
	}
		
	public Monitor monitor( String properties ) { 
		return this.monitor( properties, ConfigurationBuilder.from( Paths.get( properties ) ).build() );
	}
	
	public Monitor monitor( Configuration configuration ) {
		return this.monitor( generateId(), configuration );
	}
	
	public Monitor monitor( String id, Configuration configuration ) {
		Monitor monitor = new Monitor( id, configuration, this.scriptManager );
		this.monitors.put( monitor.getId(), monitor);
		return monitor;
	}
		
	public void unmonitor( String id ) {
		
		Monitor monitor = this.monitors.get( id );
		if ( monitor != null ) {
			monitor.stop();
			this.monitors.remove( id );
		}
		
	}
	
	private void startMonitors() {
		
		this.monitors.values().forEach( monitor -> monitor.start() );
		
		while ( hasRunningMonitor() ) {
			try {
				Thread.sleep( 100 );
			} catch (InterruptedException iex ) {
				iex.printStackTrace();
			}
		}
		
	}
	
	public void stopAll() {
		this.monitors.values().forEach( monitor -> monitor.stop() );
	}
	
	public boolean hasRunningMonitor() {
		return this.monitors.values().stream().anyMatch( m -> m.isRunning() );
	}
	
	private String generateId() {
		return UUID.randomUUID().toString();
	}
	
}
