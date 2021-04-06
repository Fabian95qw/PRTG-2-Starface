package nucom.module.prtg.server.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponent;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import nucom.module.prtg.server.sensor.Sensor;

public class StorageManager extends StarfaceComponent
{
	private static List<Sensor> Sensorlist=null;
	private static Log Sublog = null; //Log is inherited from StarfaceComponent, in order to not Override it, use a different variablename
	private static boolean isRunning = false;
	
	@Override
	public void shutdownComponent() throws Throwable 
	{
		Sublog.debug("[SM]Shutting down Component");
		Sensorlist = null;
		isRunning = false;
	}

	@Override
	public void startComponent() throws Throwable 
	{
		Sensorlist = new ArrayList<Sensor>();
		isRunning = true;
	}
	
	public void updateContext(IRuntimeEnvironment Icontext)
	{
		Sublog = Icontext.getLog();
		Sublog.debug("[SM]StorageManager OK!");
	}
	
	@Override
	protected boolean startupCondition() {return true;}

	public boolean isRunning()
	{
		return isRunning;
	}
	
	public Sensor getSensor(String Sensorname)
	{
		if(!isRunning) {return null;}
		if(Sensorname == null || Sensorname.isEmpty()) 
		{
			Sublog.debug("Sensorname is null!");
			return null;
		}
		if(Sensorlist == null) 
		{
			Sublog.debug("Sensormanager is not running!");
			return null;
		}

		for(Sensor S : Sensorlist)
		{
			if(S.getSensorname().equals(Sensorname))
			{
				return S;
			}
		}
		
		Sensor S = new Sensor(Sensorname);
		Sensorlist.add(S);
		return S;		
	}


	
}
