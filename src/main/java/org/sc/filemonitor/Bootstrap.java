package org.sc.filemonitor;

import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;

import org.sc.filemonitor.configuration.ConfigurationBuilder;
import org.sc.filemonitor.core.Engine;

public class Bootstrap {

	public static void main(String[] args) {
		
		Engine fmEngine = new Engine();
		
		fmEngine.monitor( ConfigurationBuilder.get()
											  .path( Paths.get( "W:\\_tmp" ) )
											  .events( StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE )
											  .scripts( "myscript.groovy").build() );
		
		fmEngine.monitor( ConfigurationBuilder.get()
											  .path( Paths.get( "W:\\_tmp" ) )
											  .events( StandardWatchEventKinds.ENTRY_MODIFY )
											  .scripts( "myscript_update.groovy").build() );
		
		fmEngine.startAllAndWait();
		
	}
	
}
