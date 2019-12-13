package org.sc.filemonitor.core;

import org.sc.filemonitor.configuration.Configuration;
import org.sc.filemonitor.core.script.ScriptManager;

import com.google.common.flogger.FluentLogger;

public class Monitor {

	public static final FluentLogger logger = FluentLogger.forEnclosingClass();
	
	private Configuration configuration = null;
	private MonitorThread thread        = null;
	private String        id            = null;
	
	public Monitor( String id, Configuration configuration, ScriptManager scriptManager ) {
		this.configuration = configuration;
		this.id            = id;
		this.thread        = new MonitorThread( this, scriptManager );
	}
	
	public String getId() {
		return this.id;
	}
	
	public FluentLogger getLogger() {
		return Monitor.logger;
	}
	
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	public boolean isRunning() {
		return this.thread.isRunning();
	}
	
	public void start() {
		getLogger().atInfo().log( "[%s] Starting monitor on path %s %s", getId(), getConfiguration().getPath(), getConfiguration().getEvents() );
		new Thread( this.thread ).start();
	}
	
	public void stop() {
		this.thread.stop();
	}
	
}
