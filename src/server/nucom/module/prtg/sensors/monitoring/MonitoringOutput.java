package nucom.module.prtg.sensors.monitoring;

import java.util.ArrayList;
import java.util.List;

import de.starface.core.component.StarfaceComponentProvider;
import de.starface.core.component.vcloud.MonitoringComponent;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns Monitoring Output")
public class MonitoringOutput implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="ValueList", description="",type=VariableType.LIST)
	public List<String> ValueList = null;
	
	@OutputVar(label="Output", description="Output as String",type=VariableType.STRING)
	public String Output = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		MonitoringComponent MC = (MonitoringComponent)context.provider().fetch(MonitoringComponent.class);

		ValueList = new ArrayList<String>();
		
		Output = MC.getMonitoringOutput();
				
		for(String Line : Output.split("\\r?\\n"))
		{
			ValueList.add(Line);
		}
		
				
	}//END OF EXECUTION

	
}
