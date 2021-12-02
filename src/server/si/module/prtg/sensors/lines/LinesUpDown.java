package si.module.prtg.sensors.lines;

import java.util.Collection;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.config.wire.forms.WireUnitBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.WireSettingsHandler;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns the Amount of Lines currently Online, and Offline")
public class LinesUpDown implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Lines Up", description="",type=VariableType.NUMBER)
	public  Integer LinesUp = 0;
	
	@OutputVar(label="Lines Down", description="",type=VariableType.NUMBER)
	public  Integer LinesDown = 0;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		//CallActions CA = (CallActions)context.provider().fetch(CallActions.class);

		Log log = context.getLog();
		WireSettingsHandler WSH = (WireSettingsHandler)context.provider().fetch(WireSettingsHandler.class);
		
		Collection<WireUnitBean> Lines = WSH.getProviderConnections();
		
		for(WireUnitBean Line: Lines)
		{
			log.debug("Line: " +Line.getWireName() +" ==> " +Line.getConnectionState());

			if(Line.getConnectionState().equals("on"))
			{
				LinesUp++;
			}
			else
			{
				LinesDown++;
			}
		}
				
	}//END OF EXECUTION

	
}
