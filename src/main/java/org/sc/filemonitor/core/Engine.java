package org.sc.filemonitor.core;

import java.util.HashMap;
import java.util.Map;

import org.sc.filemonitor.configuration.Configuration;

public class Engine {

	private Map<String, Monitor> monitors = new HashMap<>();
	
	public Monitor monitor( Configuration configuration ) {
		Monitor monitor = new Monitor( configuration );
		this.monitors.put( monitor.getId(), monitor);
		return monitor;
	}
	
	public void startAll() {
		this.monitors.values().forEach( monitor -> monitor.start() );
	}
	
	public void startAllAndWait() {
		
		this.startAll();
		
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
	
}
