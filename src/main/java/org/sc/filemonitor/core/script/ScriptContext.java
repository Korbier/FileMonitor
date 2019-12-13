package org.sc.filemonitor.core.script;

import org.sc.filemonitor.core.Engine;
import org.sc.filemonitor.core.Monitor;

public class ScriptContext {

	private Engine engine = null;
	
	public ScriptContext( Engine engine ) {
		this.engine = engine;
	}
	
	public Monitor monitor( String properties ) {
		return this.engine.monitor( properties );
	}
	
	public void unmonitor( String properties ) {
		this.engine.unmonitor( properties );
	}
	
}
