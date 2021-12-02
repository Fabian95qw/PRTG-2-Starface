package si.module.prtg.server.entrypoints.sensordata;

import java.util.Map;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import si.module.prtg.server.manager.StorageManager;
import si.module.prtg.server.sensor.ChannelData;
import si.module.prtg.server.sensor.Sensor;
import si.module.prtg.server.utility.EnumHelper.Unit;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Add Sensor Data to PRTG Monitor")
public class AddSensorData implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	

	@InputVar(label="Sensorname", description="Sensorname",type=VariableType.STRING)
	public String Sensorname="";
	
	@InputVar(label="Channelname", description="Channel in the Sensor",type=VariableType.STRING)
	public String Channelname="";
	
	@InputVar(label="Value", description="Value to be set in this Channel",type=VariableType.STRING)
	public String Value="";
		
	@InputVar(label="Unit", description="Unit to use for this Channel, if custom is chosen it will display your custom unit from the field \"CustomUnit\" ", valueByReferenceAllowed=true)
	public Unit U  = Unit.Custom;
		
	@InputVar(label="CustomUnit", description="CustomUnit to Display",type=VariableType.STRING)
	public String CustomUnit="";
	
	@InputVar(label="Addition Parameters", description="Addition Parameters to Add to the Channel, in the Format <Elementname, Value>",type=VariableType.MAP)
	public Map<String, String>Params = null;
	
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
			log.debug("PRTG-Service is not running");
			return;
		}
		
		Sensor S = SM.getSensor(Sensorname);
		
		ChannelData CD = S.getChannelmap().get(Channelname);
		if(CD == null)
		{
			log.debug("New Sensor: "+ Channelname);
			CD = new ChannelData(Channelname, Value, U, CustomUnit, Params);
			S.getChannelmap().put(Channelname, CD);
		}
		else
		{
			log.debug("Updating Sensor:" + Channelname);
			CD.setValue(Value);
			CD.setU(U);
			CD.setCustomUnit(CustomUnit);
			CD.setParams(Params);
		}
		
	}//END OF EXECUTION

	
}
