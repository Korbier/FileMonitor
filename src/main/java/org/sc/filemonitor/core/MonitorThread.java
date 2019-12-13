package org.sc.filemonitor.core;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.sc.filemonitor.configuration.Configuration;
import org.sc.filemonitor.core.script.Script;
import org.sc.filemonitor.core.script.ScriptManager;
import org.sc.filemonitor.core.script.ScriptType;

public class MonitorThread implements Runnable {
	
	private Monitor             monitor       = null;
	private ScriptManager       scriptManager = null;
	private boolean             running       = true;
	private Map<String, Script> scripts       = new HashMap<>();
	
	private WatchService        watchService  = null;
	private WatchKey            watchKey      = null; 
	
	public MonitorThread( Monitor monitor, ScriptManager scriptManager ) {
		this.monitor       = monitor;
		this.scriptManager = scriptManager;
		this.initializeScripts();
	}
	
	public Monitor getMonitor() {
		return this.monitor;
	}
	
	public Configuration getConfiguration() {
		return this.monitor.getConfiguration();
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public void stop() {
		
		getMonitor().getLogger().atInfo().log( "[%s] Stopping monitor on path %s %s", getMonitor().getId(), getConfiguration().getPath(), getConfiguration().getEvents() );
		
		this.running = false;
		
		if ( this.watchKey != null ) {
			this.watchKey.cancel();
		}
		
		try {
			if ( this.watchService != null ) {
				this.watchService.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {

		try ( WatchService watcher = getConfiguration().getPath().getFileSystem().newWatchService() ) {
			
			this.watchService = watcher;
			this.watchKey     = getConfiguration().getPath().register( this.watchService, getConfiguration().getEvents() );
			
			while ( isRunning() ) {
				
				try {
					
					WatchKey watchKey = this.watchService.take(); // On récupère une clef sur un événement (code bloquant)
	            
					for (WatchEvent<?> event: watchKey.pollEvents()) { // On parcourt tous les évènements associés à cette clef
						if ( event.kind() == StandardWatchEventKinds.OVERFLOW ) {
							processOverflowEvent( event );
						} else {
							processEvent( event );
						}
		            }
		            
		            if ( !watchKey.reset() ) { // On réinitialise la clef (très important pour recevoir les événements suivants)
		                stop(); // Le répertoire qu'on surveille n'existe plus ou n'est plus accessible
		            }
		            
				} catch ( ClosedWatchServiceException ex ) {
					getMonitor().getLogger().atFine().log( "[%s] Monitor stop has been requested on path %s %s", getMonitor().getId(), getConfiguration().getPath(), getConfiguration().getEvents() );
				}
	        }
			
			getMonitor().getLogger().atInfo().log( "[%s] Monitor stopped on path %s %s", getMonitor().getId(), getConfiguration().getPath(), getConfiguration().getEvents() );
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private void processOverflowEvent( WatchEvent<?> event ) {
		getMonitor().getLogger().atWarning().log( "[%s] Overflow Event occured on file %s", getMonitor().getId(), event.context() );
	}
	
	private void processEvent( WatchEvent<?> event ) {
		
		getMonitor().getLogger().atFine().log( "[%s] Event occured : %s on file %s", getMonitor().getId(), event.kind(), event.context() );
		
		for ( String scriptname : getConfiguration().getScripts() ) {
		
			Optional<Script> oScript = getScript( scriptname );
		
			if ( oScript.isPresent() ) {
		
				getMonitor().getLogger().atFine().log( "[%s] Executing script : %s", getMonitor().getId(), scriptname );
				
				Script script = oScript.get();				
				script.setVariable( "path",  getConfiguration().getPath().toString() );
				script.setVariable( "event", event.kind().toString() );
				script.setVariable( "file",  event.context().toString() );
				script.execute();
				
			}
			
		}
		
	}
	
	private void initializeScripts() {
		
		Monitor.logger.atInfo().log( "[%s] Loading scripts : ", getMonitor().getId() );
		
		for ( String script : getConfiguration().getScripts() ) {
			Monitor.logger.atInfo().log( "[%s] . Loading %s", getMonitor().getId(), script );
			getScript( script );
		}
		
	}
	
	private Optional<Script> getScript( String scriptname ) {
	
		Script script = this.scripts.get( scriptname );
		
		if ( script == null ) {
			
			String [] parts = scriptname.split( ":" );
			
			if ( parts.length != 2 ) {
				Monitor.logger.atSevere().log("[%s] \"%s\" is not a valid script name. valid name : \"groovy:my-script.groovy\"", getMonitor().getId(), scriptname );
				return Optional.empty();
			}
			
			Optional<ScriptType> oType = ScriptType.findByPrefix( parts[0] );
			if ( !oType.isPresent() ) {
				Monitor.logger.atSevere().log("[%s] \"%s\" is not a valid script type. valid type are : \"groovy\", \"java\"", getMonitor().getId(), parts[0] );
				return Optional.empty();				
			}
			
			Optional<Script> oScript = this.scriptManager.get( oType.get(), parts[1] );
			if ( !oScript.isPresent() ) {
				Monitor.logger.atSevere().log("[%s] Script \"%s\" not found", getMonitor().getId(), scriptname );
				return Optional.empty();
			}
			
			script = oScript.get();
			this.scripts.put( scriptname, oScript.get() );
			
		}
		
		return Optional.of( script );
		
	}
}
