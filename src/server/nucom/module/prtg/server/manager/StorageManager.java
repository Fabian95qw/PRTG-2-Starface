package nucom.module.prtg.server.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponent;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import nucom.module.prtg.server.listener.ConnectionListener;
import nucom.module.prtg.server.xml.XMLConstructor;

public class StorageManager extends StarfaceComponent
{
	private Map<String, XMLConstructor> Sensormap = null; //MAP <Sensorname, Channeldata>
	private ConnectionListener CL = null;
	private Log Sublog = null; //Log is inherited from StarfaceComponent, in order to not Override it, use a different variablename
	private boolean isRunning = false;
	
	@Override
	public void shutdownComponent() throws Throwable 
	{
		Sublog.debug("[SM]Shutting down Component");
		Sensormap = null;
		if(CL != null)
		{
			CL.shutdown();
		}
		isRunning = false;
	}

	@Override
	public void startComponent() throws Throwable 
	{
		Sensormap = new HashMap<String, XMLConstructor>();
		isRunning = true;
	}
	
	public void updateContext(IRuntimeEnvironment Icontext)
	{
		Sublog = Icontext.getLog();
		Sublog.debug("[SM]StorageManager OK!");
	}
	
	@Override
	protected boolean startupCondition() {return true;}

	public ConnectionListener getConnectionListener() {
		return CL;
	}

	public void setConnectionListener(ConnectionListener CL) {
		this.CL = CL;
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	
	public XMLConstructor getPackage(String Sensor)
	{
		if(Sensor == null || Sensor.isEmpty()) 
		{
			Sublog.debug("Sensorname is null!");
			return null;
		}
		if(Sensormap == null) 
		{
			Sublog.debug("Sensormanager is not running!");
			return null;
		}
		XMLConstructor XMLC = Sensormap.get(Sensor);
		if(XMLC == null)
		{
			XMLC = new XMLConstructor(Sublog);
			Sensormap.put(Sensor, XMLC);
		}
		return XMLC;
	}
	
}
