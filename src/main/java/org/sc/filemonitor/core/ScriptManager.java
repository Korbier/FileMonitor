package org.sc.filemonitor.core;

import java.io.IOException;

import groovy.util.GroovyScriptEngine;

public class ScriptManager {

	private GroovyScriptEngine engine = null;
	
	public ScriptManager() {
		try {
			this.engine = new GroovyScriptEngine( "data/script" );
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
}
