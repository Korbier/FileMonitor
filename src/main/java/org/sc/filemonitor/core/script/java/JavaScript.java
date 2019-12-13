package org.sc.filemonitor.core.script.java;

import org.sc.filemonitor.core.script.Script;
import org.sc.filemonitor.core.script.ScriptType;

public class JavaScript implements Script {

	private JavaScriptHandler handler = null;
	
	public JavaScript( JavaScriptHandler handler ) {
		this.handler = handler;
	}
	
	@Override
	public ScriptType getType() {
		return ScriptType.JAVA;
	}

	@Override
	public void setVariable(String name, Object value) {
		this.handler.setVariable(name, value);		
	}

	@Override
	public void execute() {
		this.handler.run();
	}

}
