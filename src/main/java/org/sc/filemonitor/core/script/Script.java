package org.sc.filemonitor.core.script;


public interface Script {

	public ScriptType getType();
	public void setVariable( String name, Object value );
	public void execute();
	
}
