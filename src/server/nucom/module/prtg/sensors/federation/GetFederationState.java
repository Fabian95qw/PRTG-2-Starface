package nucom.module.prtg.sensors.federation;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.federation.FederationManager;
import de.vertico.starface.federation.site.SiteInfo;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns the amount of online/offline federations ")
public class GetFederationState implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Online", description="Locations Online",type=VariableType.NUMBER)
	public  Integer Online=0;
	
	@OutputVar(label="Offline", description="Locations Offline",type=VariableType.NUMBER)
	public  Integer Offline=0;
	
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		FederationManager FM = (FederationManager)context.provider().fetch(FederationManager.class);
		for(SiteInfo S : FM.getAllSites())
		{
			if(S.isOnline())
			{
				Online++;
			}
			else
			{
				Offline++;
			}
		}
		
		
		
	}//END OF EXECUTION

	
}
