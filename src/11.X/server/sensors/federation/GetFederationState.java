package si.module.prtg.sensors.federation;


import de.vertico.starface.federation.FederationManager;
import de.vertico.starface.federation.site.SiteInfo;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, description="Returns the amount of online/offline federations ")
public class GetFederationState implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Online", description="Locations Online",type=VariableType.NUMBER)
	public  Integer Online=0;
	
	@OutputVar(label="Offline", description="Locations Offline",type=VariableType.NUMBER)
	public  Integer Offline=0;
	
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		FederationManager FM = (FederationManager)context.springApplicationContext().getBean(FederationManager.class);
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
