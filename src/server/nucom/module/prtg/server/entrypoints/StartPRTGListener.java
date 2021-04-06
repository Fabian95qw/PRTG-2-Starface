package nucom.module.prtg.server.entrypoints;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import nucom.module.prtg.server.manager.StorageManager;
import nucom.module.prtg.server.utility.LogHelper;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Starting a PRTG Listener. Does nothing if listener is already running")
public class StartPRTGListener implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	
		
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		//Checking if there is already a Connection Listener existing, and still active
		
		StorageManager SM = (StorageManager)context.provider().fetch(StorageManager.class);
		
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
