package nucom.module.prtg.client.config;

import java.io.File;
import java.util.Properties;

import nucom.module.prtg.client.config.EnumHelper.Config;


public class ConfigManager 
{
	//Simple Config/Property Manager, which allows any casts
	
	Properties P = new Properties();
	File ConfigFile = null;

	public void put(Config CF, Object CV)
	{
		//Put a Property from the Config in
		P.put(CF.toString(), CV);
	}
	
	public String S(Config CF)
	{
		//Tries to cast the property to a String
		return (String)P.getProperty(CF.toString());
	}
	
	public Integer I(Config CF)
	{
		//Tries to cast the property to an integer
		return Integer.parseInt(P.getProperty(CF.toString()));
	}
	
	public boolean B(Config CF)
	{
		//Tries to cast the properte to a boolean
		return Boolean.parseBoolean(P.getProperty(CF.toString()));
	}

}

