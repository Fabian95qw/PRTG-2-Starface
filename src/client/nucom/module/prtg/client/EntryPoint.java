package nucom.module.prtg.client;

import nucom.module.prtg.client.config.ConfigManager;
import nucom.module.prtg.client.config.EnumHelper.Config;
import nucom.module.prtg.client.connection.Connection;
import nucom.module.prtg.client.logger.Logger;

public class EntryPoint 
{
	public static ConfigManager CM = null;
	
	public static void main(String[] args) 
	{
		if(args.length < 4)
		{
			System.out.println("Not Enough arguments");
			return;
		}	
		
		CM = new ConfigManager();
		
		parseArgs(args);

		Logger l = new Logger("EntryPoint", CM.S(Config.LogLocation));
		
		l.log("ConfigFile loaded");
		
		if(CM.S(Config.IPorDNS).isEmpty())
		{
			l.log("No Valid DNS-Entry or IP in Config file. Shutting down");
			return;
		}
				
		l.log("Opeing Connection to: " + CM.S(Config.IPorDNS) +" on Port: " + CM.I(Config.Port));
		
		Connection C = new Connection(CM.S(Config.IPorDNS), CM.I(Config.Port), CM.S(Config.Password));
		C.Open();
	}
	
	private static void parseArgs(String[] args)
	{
		
		String IPorDNS = args[0];
		String Port = args[1];
		String Password = args[2];
		String TrustallCA = args[3];		
		String Loglocation = "";
		
		if(args.length >=5)
		{
			Loglocation = args[4];
		}
				
		CM.put(Config.IPorDNS, IPorDNS);
		CM.put(Config.Port, Port);
		CM.put(Config.Password, Password);
		CM.put(Config.LogLocation, Loglocation);
		CM.put(Config.TrustallCA, TrustallCA);
		
	}

}
