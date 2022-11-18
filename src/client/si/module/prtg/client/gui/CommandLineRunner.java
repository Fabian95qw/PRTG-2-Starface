package si.module.prtg.client.gui;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import si.module.prtg.client.utility.Log;
import si.module.prtg.client.utility.LogHelper;
import si.module.prtg.client.xml.XMLConstructor;
import si.module.prtg.client.xmlrpc.Result;
import si.module.prtg.client.xmlrpc.XmlRpcConnector;
import si.module.prtg.client.xmlrpc.commands.sensor.Sensor;

public class CommandLineRunner 
{
	private Log log = null;

	public CommandLineRunner()
	{
		
	}
	
	public void Run(String[] args)
	{
		Options Opt = new Options();

		{
		Option O = new Option("h", "host", true, "STARFACE Host");
		O.setRequired(true);
		Opt.addOption(O);
		O = new Option("t","token", true, "STARFACE Token");
		O.setRequired(true);
		Opt.addOption(O);
		O = new Option("s","sensorname", true, "Sensorname");
		O.setRequired(true);
		Opt.addOption(O);
		O = new Option("i","instancename", true, "Instancename");
		O.setRequired(true);
		Opt.addOption(O);
		O = new Option("ssl","ssl", true, "Use SSL Connection");
		O.setRequired(true);
		Opt.addOption(O);
		O = new Option("d","debug", true, "Disable Debugging");
		O.setRequired(true);
		Opt.addOption(O);
		O = new Option("p","port", true, "Use custom Port");
		O.setRequired(false);
		Opt.addOption(O);
		
		}
		
		/*
		O.addRequiredOption("h","host", true, "STARFACE Host");
		O.addRequiredOption("t","token", true, "STARFACE Token");
		O.addRequiredOption("s","sensorname", true, "Sensorname");
		O.addRequiredOption("i","instancename", true, "Instancename");
		O.addRequiredOption("d","debug", true, "Disable Debugging");
		*/
		
		CommandLineParser CMP = new DefaultParser();
		CommandLine CMD =null;
		try 
		{
			CMD = CMP.parse(Opt, args);
		} 
		catch (ParseException e) 
		{
			System.out.println("ERROR WHILE PARSING ARGUMENTS");
			System.out.println(e.getMessage());
			
			for(Option O : Opt.getOptions())
			{
				System.out.println(O.toString());
			}
			return;
		}
		
		if(!CMD.hasOption("d"))
		{
			Log.Disable();
		}
		else
		{
			String SBool = CMD.getOptionValue("d");
			try
			{
				Boolean Debug = Boolean.parseBoolean(SBool);
				if(!Debug)
				{
					Log.Disable();
				}
			}
			catch(Exception e)
			{
				Log.Disable();
			}
		}
		

		log = new Log(this.getClass());
		log.debug("Initialized CommandLineRunner");
		log.debug("Arguments:");
		
		String Host = CMD.getOptionValue("h");
		String Token = CMD.getOptionValue("t");
		String Sensorname = CMD.getOptionValue("s");
		String Instancename = CMD.getOptionValue("i");
		String SUseSSL = CMD.getOptionValue("ssl");
		boolean UseSSL = Boolean.valueOf(SUseSSL);
		Integer Port = -1;
		try
		{
			String SPort = CMD.getOptionValue("p");
			Port = Integer.parseInt(SPort);
			Host=Host+":"+Port;
		}
		catch(Exception e)
		{
			log.debug(e.getMessage());
		}
		
		log.debug("Host: " + Host);
		log.debug("Port: "+ Port);
		log.debug("Token:" + Token);
		log.debug("Sensorname:"+  Sensorname);		
		
		
		try
		{
			XmlRpcConnector XMLRPC = new XmlRpcConnector(Instancename, Host, Token, UseSSL);
			Sensor SC = new Sensor(Sensorname);
			XMLRPC.execute(SC);
			
			Result R = SC.Result();
			
			if(R.Success())
			{
				log.debug("Request was Successful!");
				log.debug(R.Data().toString());
				
				XMLConstructor XMLC = new XMLConstructor();	
				String XML = XMLC.ConstructXML(R.Data());
				System.out.println(XML);
			}
			else
			{
				log.debug("ERROR! " + R.Errormessage());
			}
			
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		
		
		
	}
}
