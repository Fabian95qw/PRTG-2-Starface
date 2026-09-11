package si.module.prtg.server.entrypoints;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import si.module.prtg.server.manager.StorageManager;
import si.module.prtg.server.utility.LogHelper;

@Function(visibility=Visibility.Private, description="Starting a PRTG Listener. Does nothing if listener is already running")
public class StartPRTGListener implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	
		
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		//Checking if there is already a Connection Listener existing, and still active
		
		StorageManager SM = new StorageManager();
		
		if(!SM.isRunning())
		{
			log.debug("Starting StorageManager");
			try 
			{
				SM.startComponent();
				SM.updateContext(context);
			} 
			catch (Throwable e) 
			{
				LogHelper.EtoStringLog(log, e);
			}
		}
		
		
	}//END OF EXECUTION

	
}
