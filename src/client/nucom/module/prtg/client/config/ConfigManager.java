package nucom.module.prtg.client.config;

import java.io.File;
import java.util.Properties;

import nucom.module.prtg.client.config.EnumHelper.Config;


public class ConfigManager 
{
	Properties P = new Properties();
	File ConfigFile = null;

	public void put(Config CF, Object CV)
	{
		P.put(CF.toString(), CV);
	}
	
	public String S(Config CF)
	{
		return (String)P.getProperty(CF.toString());
	}
	
	public Integer I(Config CF)
	{
		return Integer.parseInt(P.getProperty(CF.toString()));
	}
	
	public boolean B(Config CF)
	{
		return Boolean.parseBoolean(P.getProperty(CF.toString()));
	}

}

