package nucom.module.prtg.client.xmlrpc.commands.sensor;

import java.util.Collections;

import nucom.module.prtg.client.utility.EnumHelper.Commands;
import nucom.module.prtg.client.xmlrpc.commands.BasicCommand;

public class Sensor extends BasicCommand
{

	public Sensor(String Sensorname) 
	{
		super(Commands.sensor, Collections.singletonMap("Sensorname", Sensorname));
	}

}
