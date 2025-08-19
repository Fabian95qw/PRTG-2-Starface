package si.module.prtg.sensors.monitoring;

import java.util.HashMap;
import java.util.Map;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns Current CPU Usage as a Map<Core, Usage>")
public class CPUUsage implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Usage", description="",type=VariableType.MAP)
	public Map<String, String> Usage = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{	
		Usage = new HashMap<String, String>();
		
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		CentralProcessor cpu = hal.getProcessor();
		
		long[][] prevTicks = cpu.getProcessorCpuLoadTicks();		
		Thread.sleep(1000);				
		double[] loads = cpu.getProcessorCpuLoadBetweenTicks(prevTicks);	
		
		Integer Core = 0;
		for(double d : loads)
		{
			Usage.put(""+Core, ""+Math.round(d*100));
			Core++;
		}
				
	}//END OF EXECUTION

	
}
