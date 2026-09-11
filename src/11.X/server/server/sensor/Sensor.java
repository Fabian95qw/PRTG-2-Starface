package si.module.prtg.server.sensor;

import java.util.HashMap;
import java.util.Map;

public class Sensor 
{
	private String Sensorname="";
	private Map<String, ChannelData> Channelmap = new HashMap<String, ChannelData>();
	
	public String getSensorname() {
		return Sensorname;
	}

	public Map<String, ChannelData> getChannelmap() {
		return Channelmap;
	}

	public Sensor(String Sensorname)
	{
		this.Sensorname=Sensorname;
	}

}
