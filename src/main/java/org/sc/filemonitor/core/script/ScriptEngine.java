package org.sc.filemonitor.core.script;

import java.util.Optional;

public interface ScriptEngine {

	public Optional<Script> getScript( ScriptContext context, String scriptname );
	
}
