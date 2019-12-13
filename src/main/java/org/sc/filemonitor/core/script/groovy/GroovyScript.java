package org.sc.filemonitor.core.script.groovy;

import org.sc.filemonitor.core.script.Script;
import org.sc.filemonitor.core.script.ScriptType;

public class GroovyScript implements Script {

	private groovy.lang.Script _script = null;
	
	public GroovyScript( groovy.lang.Script _script ) {
		this._script = _script;
	}
	
	public ScriptType getType() {
		return ScriptType.GROOVY;
	}
	
	public void setVariable( String name, Object value ) {
		this._script.getBinding().setVariable(name, value);
	}
	
	public void execute() {
		this._script.run();
	}
	
}
