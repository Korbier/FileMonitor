package org.sc.filemonitor.core.script.groovy;

import java.io.IOException;
import java.util.Optional;

import org.sc.filemonitor.core.script.Script;
import org.sc.filemonitor.core.script.ScriptContext;
import org.sc.filemonitor.core.script.ScriptEngine;

import groovy.lang.Binding;

public class GroovyScriptEngine implements ScriptEngine {

	private groovy.util.GroovyScriptEngine _engine = null;
	
	public GroovyScriptEngine() {
		try {
			this._engine = new groovy.util.GroovyScriptEngine( "data/script" );
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public Optional<Script> getScript(ScriptContext context, String scriptname) {
		
		try {
			
			groovy.lang.Script gScript = this._engine.createScript( scriptname, new Binding() );
			gScript.getBinding().setVariable( "context", context );
			
			if ( gScript != null ) {
				return Optional.of( new GroovyScript( gScript ) );
			}
					
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return Optional.empty();
		
	}

}
