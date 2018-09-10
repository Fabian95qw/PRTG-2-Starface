package nucom.module.prtg.client;

import nucom.module.prtg.client.config.ConfigManager;
import nucom.module.prtg.client.config.EnumHelper.Config;
import nucom.module.prtg.client.connection.Connection;
import nucom.module.prtg.client.logger.Logger;

public class EntryPoint 
{
	public static ConfigManager CM = null;
	
	
	/*
NNNNNNNN        NNNNNNNN                                                                                                   AAA                  GGGGGGGGGGGGG
N:::::::N       N::::::N                                                                                                  A:::A              GGG::::::::::::G
N::::::::N      N::::::N                                                                                                 A:::::A           GG:::::::::::::::G
N:::::::::N     N::::::N                                                                                                A:::::::A         G:::::GGGGGGGG::::G
N::::::::::N    N::::::Nuuuuuu    uuuuuu      cccccccccccccccc   ooooooooooo      mmmmmmm    mmmmmmm                   A:::::::::A       G:::::G       GGGGGG
N:::::::::::N   N::::::Nu::::u    u::::u    cc:::::::::::::::c oo:::::::::::oo  mm:::::::m  m:::::::mm                A:::::A:::::A     G:::::G              
N:::::::N::::N  N::::::Nu::::u    u::::u   c:::::::::::::::::co:::::::::::::::om::::::::::mm::::::::::m              A:::::A A:::::A    G:::::G              
N::::::N N::::N N::::::Nu::::u    u::::u  c:::::::cccccc:::::co:::::ooooo:::::om::::::::::::::::::::::m             A:::::A   A:::::A   G:::::G    GGGGGGGGGG
N::::::N  N::::N:::::::Nu::::u    u::::u  c::::::c     ccccccco::::o     o::::om:::::mmm::::::mmm:::::m            A:::::A     A:::::A  G:::::G    G::::::::G
N::::::N   N:::::::::::Nu::::u    u::::u  c:::::c             o::::o     o::::om::::m   m::::m   m::::m           A:::::AAAAAAAAA:::::A G:::::G    GGGGG::::G
N::::::N    N::::::::::Nu::::u    u::::u  c:::::c             o::::o     o::::om::::m   m::::m   m::::m          A:::::::::::::::::::::AG:::::G        G::::G
N::::::N     N:::::::::Nu:::::uuuu:::::u  c::::::c     ccccccco::::o     o::::om::::m   m::::m   m::::m         A:::::AAAAAAAAAAAAA:::::AG:::::G       G::::G
N::::::N      N::::::::Nu:::::::::::::::uuc:::::::cccccc:::::co:::::ooooo:::::om::::m   m::::m   m::::m        A:::::A             A:::::AG:::::GGGGGGGG::::G
N::::::N       N:::::::N u:::::::::::::::u c:::::::::::::::::co:::::::::::::::om::::m   m::::m   m::::m       A:::::A               A:::::AGG:::::::::::::::G
N::::::N        N::::::N  uu::::::::uu:::u  cc:::::::::::::::c oo:::::::::::oo m::::m   m::::m   m::::m      A:::::A                 A:::::A GGG::::::GGG:::G
NNNNNNNN         NNNNNNN    uuuuuuuu  uuuu    cccccccccccccccc   ooooooooooo   mmmmmm   mmmmmm   mmmmmm     AAAAAAA                   AAAAAAA   GGGGGG   GGGG 
	 */
	
	/*
	 * NOTE:
	 * Any use of System.out.prinln will cause a malformed XML exception in the prtg monitor!
	 * 
	 */
	
	public static void main(String[] args) 
	{
		//Check if enough arguments are present
		if(args.length < 4)
		{
			System.out.println("Not Enough arguments");
			return;
		}	
		
		//Create the config manager instance, which will be used globally.
		CM = new ConfigManager();
		
		parseArgs(args);

		Logger l = new Logger("EntryPoint", CM.S(Config.LogLocation));
								
		l.log("Opeing Connection to: " + CM.S(Config.IPorDNS) +" on Port: " + CM.I(Config.Port));
		
		//Creating the Connection and open it.
		Connection C = new Connection(CM.S(Config.IPorDNS), CM.I(Config.Port), CM.S(Config.Password), CM.S(Config.Sensor));
		C.Open();
	}
	
	private static void parseArgs(String[] args)
	{
		//Parsing all Arg's
		String IPorDNS = args[0];
		String Port = args[1];
		String Password = args[2];
		String Sensor = args[3];
		String TrustallCA = args[4];		
		String Loglocation = "";
		
		if(args.length >=6)
		{
			Loglocation = args[5];
		}
				
		CM.put(Config.IPorDNS, IPorDNS);
		CM.put(Config.Port, Port);
		CM.put(Config.Password, Password);
		CM.put(Config.Sensor, Sensor);
		CM.put(Config.LogLocation, Loglocation);
		CM.put(Config.TrustallCA, TrustallCA);
		
	}

}
