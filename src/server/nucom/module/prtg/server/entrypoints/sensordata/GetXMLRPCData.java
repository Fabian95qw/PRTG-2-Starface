package nucom.module.prtg.server.entrypoints.sensordata;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.prtg.server.manager.StorageManager;
import nucom.module.prtg.server.sensor.ChannelData;
import nucom.module.prtg.server.sensor.Sensor;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns the Data-Package for the XML-RPC interface")
public class GetXMLRPCData implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	
	
	@InputVar(label="Sensorname", description="",type=VariableType.STRING)
	public String Sensorname="";
	
	@OutputVar(label="Data", description="",type=VariableType.MAP)
	public Map<String,Map<String,Object>> Data = new HashMap<String,Map<String,Object>>();
	
	@OutputVar(label="Errormessage", description="",type=VariableType.STRING)
	public String Errormessage="";
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		StorageManager SM = (StorageManager)context.provider().fetch(StorageManager.class);
		Log log = context.getLog();
		
		if(!SM.isRunning())
		{
			Errormessage="PRTG-Service is not running";
			log.debug("PRTG-Service is not running");
			return;
		}
		
		Sensor S = SM.getSensor(Sensorname);
		if(S == null)
		{
			Errormessage="Sensor does not exist!";
			log.debug("Sensor does not exist!");
			return;
		}
		
		Map<String, ChannelData> SensorMap = S.getChannelmap();
		if(SensorMap == null)
		{
			Errormessage = "Sensor does not have any channels!";
			return;
		}
		for(Entry<String, ChannelData> Entry : SensorMap.entrySet())
		{
			Map<String, Object> ChannelDataMap = new HashMap<String, Object>();
			ChannelDataMap.put("Channel", Entry.getValue().getChannel());
			ChannelDataMap.put("Value", Entry.getValue().getValue());
			ChannelDataMap.put("Unit",  Entry.getValue().getU().toString());
			ChannelDataMap.put("CustomUnit",  Entry.getValue().getCustomUnit());
			ChannelDataMap.put("Params",  Entry.getValue().getParams());
			Data.put(Entry.getKey(), ChannelDataMap);
		}
		
		Success = true;
		
	}//END OF EXECUTION

	
}
