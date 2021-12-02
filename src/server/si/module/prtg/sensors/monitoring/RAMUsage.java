package si.module.prtg.sensors.monitoring;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns Current RAM Usage")
public class RAMUsage implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Usage[MB]", description="",type=VariableType.NUMBER)
	public Integer UsageMB=0;
	
	@OutputVar(label="Free[MB]", description="",type=VariableType.NUMBER)
	public Integer FreeMB=0;
	
	@OutputVar(label="Usage[%]", description="",type=VariableType.NUMBER)
	public Integer UsagePercent=0;
	
	@OutputVar(label="Free[%]", description="",type=VariableType.NUMBER)
	public Integer FreePercent=0;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{			
		Sigar S = new Sigar();
				
		Mem M = S.getMem();
		
		UsageMB = (int) M.getActualUsed();
		UsageMB = UsageMB / 1024;
		UsageMB = UsageMB / 1024;
		FreeMB = (int) M.getActualFree();
		FreeMB = FreeMB / 1024;
		FreeMB = FreeMB / 1024;
		
		UsagePercent = (int) M.getUsedPercent();
		FreePercent = 100-UsagePercent;
		
	}//END OF EXECUTION

	
}
