package si.module.prtg.server.entrypoints;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import si.module.prtg.server.manager.StorageManager;
import si.module.prtg.server.utility.LogHelper;
@Function(visibility=Visibility.Private, description="Stops the PRTG Listener.")
public class StopPRTGListener implements IBaseExecutable 
{
	//##########################################################################################
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
				
		StorageManager SM = new StorageManager();
		
		if(SM.isRunning())
		{
			log.debug("Shutting down StorageManager");
			try
			{
				SM.shutdownComponent();
			}
			catch (Throwable e)
			{
				LogHelper.EtoStringLog(log, e);
			}
		}
	}//END OF EXECUTION

	
}
