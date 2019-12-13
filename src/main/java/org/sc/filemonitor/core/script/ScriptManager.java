package org.sc.filemonitor.core.script;

import java.util.Optional;

public class ScriptManager {

	private ScriptContext context = null;
	
	public ScriptManager( ScriptContext context ) {
		this.context = context;
	}
	
	public Optional<Script> get( ScriptType type,  String filename  ) {
		return type.getEngine().getScript( this.context, filename );
	}
	
}
