package si.module.prtg.sensors.monitoring;


import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import oshi.hardware.GlobalMemory;
import oshi.hardware.platform.linux.LinuxHardwareAbstractionLayer;

@Function(visibility=Visibility.Private, description="Returns Current RAM Usage")
public class RAMUsage implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Usage[MB]", description="",type=VariableType.NUMBER)
	public Long UsageMB=0L;
	
	@OutputVar(label="Free[MB]", description="",type=VariableType.NUMBER)
	public Long FreeMB=0L;
	
	@OutputVar(label="Usage[%]", description="",type=VariableType.NUMBER)
	public Integer UsagePercent=0;
	
	@OutputVar(label="Free[%]", description="",type=VariableType.NUMBER)
	public Integer FreePercent=0;
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{		
		LinuxHardwareAbstractionLayer LHAL = new LinuxHardwareAbstractionLayer();
		GlobalMemory GM = LHAL.createMemory();
		
		Long Total = GM.getTotal();
		Long Available = GM.getAvailable();
		Long Used = Total -Available;
		
		UsageMB = (Used /1024)/1024;
		FreeMB = (Available /1024)/1024;
		
		Float Percentage = 100F/Total;
		Float FUsagePercent = Used * Percentage;
		UsagePercent = FUsagePercent.intValue();
		FreePercent = 100-UsagePercent;
				
	}//END OF EXECUTION

	
}
