package org.sc.filemonitor.core.script.java;

import java.util.Optional;

import org.sc.filemonitor.core.script.Script;
import org.sc.filemonitor.core.script.ScriptContext;
import org.sc.filemonitor.core.script.ScriptEngine;

public class JavaScriptEngine implements ScriptEngine {

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Script> getScript(ScriptContext context, String scriptname) {
		
		try {
			Class<JavaScriptHandler> clazz   = (Class<JavaScriptHandler>) Class.forName( scriptname );
			JavaScriptHandler        handler = clazz.newInstance();
			handler.setVariable( "context", context );
			return Optional.of( new JavaScript( handler ) );
		} catch (Exception ex) {
			ex.printStackTrace();
			return Optional.empty();
		}
		
	}

}
