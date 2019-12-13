package org.sc.filemonitor.core.script;

import java.util.Arrays;
import java.util.Optional;

import org.sc.filemonitor.core.script.groovy.GroovyScriptEngine;
import org.sc.filemonitor.core.script.java.JavaScriptEngine;

public enum ScriptType {
	
	GROOVY( "groovy", new GroovyScriptEngine() ),
	JAVA( "java",     new JavaScriptEngine() );
	
	private String       prefix = null;
	private ScriptEngine engine = null;
	
	private ScriptType( String prefix, ScriptEngine engine ) {
		this.prefix = prefix;
		this.engine = engine;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public ScriptEngine getEngine() {
		return this.engine;
	}
	
	public static Optional<ScriptType> findByPrefix( String prefix ) {
		return Arrays.stream(values()).filter( v -> v.getPrefix().equals( prefix ) ).findAny();
	}
	
	
}
