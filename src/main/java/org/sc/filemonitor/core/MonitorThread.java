package org.sc.filemonitor.core;

import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.sc.filemonitor.configuration.Configuration;

public class MonitorThread implements Runnable {
	
	private Configuration configuration = null;
	private boolean       running       = true;
	
	public MonitorThread( Configuration configuration ) {
		this.configuration = configuration;
	}
	
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public void stop() {
		this.running = false;
	}
	
	@Override
	public void run() {

		try ( WatchService watcher = getConfiguration().getPath().getFileSystem().newWatchService() ) {
			
			getConfiguration().getPath().register( watcher, getConfiguration().getEvents() );
			
			while ( isRunning() ) {
				
				WatchKey watchKey = watcher.take(); // On récupère une clef sur un événement (code bloquant)
	            
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
	        }
	        
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private void processOverflowEvent( WatchEvent<?> event ) {
		System.out.println(event.kind() + " - " + event.context());
	}
	
	private void processEvent( WatchEvent<?> event ) {
		Monitor.logger.atInfo().log("[%s] %s : %s", event.kind(), event.context(), getConfiguration().getScripts() );
	}
	
}
