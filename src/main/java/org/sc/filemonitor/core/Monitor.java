package org.sc.filemonitor.core;

import java.util.UUID;

import org.sc.filemonitor.configuration.Configuration;

import com.google.common.flogger.FluentLogger;

public class Monitor {

	public static final FluentLogger logger = FluentLogger.forEnclosingClass();
	
	private Configuration configuration = null;
	private MonitorThread thread        = null;
	private String        id            = null;
	
	public Monitor( Configuration configuration ) {
		this.configuration = configuration;
		this.thread        = new MonitorThread( this.configuration );
		this.id            = UUID.randomUUID().toString();
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
		getLogger().atInfo().log( "[%s] Stopping monitor on path %s %s", getId(), getConfiguration().getPath(), getConfiguration().getEvents() );
		this.thread.stop();
	}
	
}
