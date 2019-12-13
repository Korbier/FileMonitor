package org.sc.filemonitor.core.script.java;

import java.util.HashMap;
import java.util.Map;

public abstract class JavaScriptHandler {

	private Map<String, Object> variables = new HashMap<>();
	
	public void setVariable( String name, Object value ) {
		this.variables.put( name, value);
	}
	
	protected Object getVariable( String name ) {
		return this.variables.get( name );
	}
	
	public abstract void run();
	
}
