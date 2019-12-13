package org.sc.filemonitor.script;

import org.sc.filemonitor.core.script.java.JavaScriptHandler;


public class ConsoleOutput extends JavaScriptHandler {

	@Override
	public void run() {
		System.out.print( "path  : " + getVariable( "path" ) );
		System.out.print( ", event : " + getVariable( "event" ) );
		System.out.println( ", file  : " + getVariable( "file" ) );
	}

}
