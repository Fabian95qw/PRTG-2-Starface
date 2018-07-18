package nucom.module.prtg.sensors.monitoring;

import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

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
		
		Sigar S = new Sigar();
		CpuPerc[] CPUList = S.getCpuPercList();
		
		Integer Counter = 0;
		
		for(CpuPerc CPU : CPUList)
		{
			Integer Summe = (int) (CPU.getCombined()*100);
			Usage.put(""+Counter, ""+Summe);
			Counter++;
		}
				
	}//END OF EXECUTION

	
}
