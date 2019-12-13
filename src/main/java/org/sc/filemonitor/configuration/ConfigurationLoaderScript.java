package org.sc.filemonitor.configuration;

import java.io.File;
import java.nio.file.StandardWatchEventKinds;

import org.sc.filemonitor.core.Monitor;
import org.sc.filemonitor.core.script.ScriptContext;
import org.sc.filemonitor.core.script.java.JavaScriptHandler;


public class ConfigurationLoaderScript extends JavaScriptHandler {

	@Override
	public void run() {
		
		if ( getVariable("event").equals( StandardWatchEventKinds.ENTRY_CREATE.toString() ) ) {
		
			ScriptContext context = (ScriptContext) getVariable( "context" );
			Monitor       monitor = context.monitor( getVariable( "path" ).toString() + File.separatorChar + getVariable( "file" ).toString() );
			
			monitor.start();
		
		} else if ( getVariable("event").equals( StandardWatchEventKinds.ENTRY_DELETE.toString() ) ) {
			
			ScriptContext context = (ScriptContext) getVariable( "context" );
			context.unmonitor( getVariable( "path" ).toString() + File.separatorChar + getVariable( "file" ).toString() );
			
		}		
	}

}
