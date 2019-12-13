package org.sc.filemonitor;

import org.sc.filemonitor.core.Engine;

public class Bootstrap {

	public static void main(String[] args) {
		
		Engine fmEngine = new Engine();
		
		fmEngine.start();
		
	}
	
}
