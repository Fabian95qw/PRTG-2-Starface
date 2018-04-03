package nucom.module.prtg.server.entrypoints;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import nucom.module.prtg.server.utility.Storage;

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
		Log log = context.getLog();
				
		if(Storage.CL != null && Storage.CL.isRunning() )
		{
			log.debug("Stopping Connection Listener");
			Storage.CL.shutdown();
		}

		Storage.XMLC = null;
	}//END OF EXECUTION

	
}
