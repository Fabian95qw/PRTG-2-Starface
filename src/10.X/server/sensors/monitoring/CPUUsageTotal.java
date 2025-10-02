package si.module.prtg.sensors.monitoring;


import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

@Function(visibility=Visibility.Private, description="Returns Current Total CPU usage")
public class CPUUsageTotal implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Usage", description="",type=VariableType.STRING)
	public String UsageTotal = "";
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{	

		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		CentralProcessor cpu = hal.getProcessor();
		
		long[] prevTicks = cpu.getSystemCpuLoadTicks();
		Thread.sleep(1000);
		double Load = cpu.getSystemCpuLoadBetweenTicks(prevTicks);
		UsageTotal = ""+Math.round(Load*100);
		
	}//END OF EXECUTION

	
}
