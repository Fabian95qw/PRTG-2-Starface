package si.module.prtg.sensors.xmpp;

import de.starface.bo.ServerBusinessObject;
import de.starface.core.component.StarfaceComponentProvider;
import de.starface.rest.server.model.PbxUtilizationRest;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns the amount of total and unique XMPP Users")
public class XMPPLogins implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Unique Users", description="",type=VariableType.NUMBER)
	public  Integer UniqueUsers=0;
	
	@OutputVar(label="Logged in Apps", description="",type=VariableType.NUMBER)
	public  Integer LoggedeinApps=0;
	
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		ServerBusinessObject SBO = context.provider().fetch(ServerBusinessObject.class);
		PbxUtilizationRest Util = SBO.getPbxUtilization();
		
		UniqueUsers=Util.getLoggedClientUsers();
		LoggedeinApps=Util.getUccClients();
	
	}//END OF EXECUTION

	
}
