package si.module.prtg.server.entrypoints;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import si.module.prtg.server.manager.StorageManager;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Stops the PRTG Listener.")
public class StopPRTGListener implements IBaseExecutable 
{
	//##########################################################################################
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
				
		StorageManager SM = (StorageManager)context.provider().fetch(StorageManager.class);
		
		if(SM.isRunning())
		{
			log.debug("Shutting down StorageManager");
			SM.shutdown();
		}
	}//END OF EXECUTION

	
}
