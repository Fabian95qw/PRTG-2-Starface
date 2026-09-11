package si.module.prtg.client.gui;

import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import si.module.prtg.client.xmlrpc.Result;
import si.module.prtg.client.xmlrpc.XmlRpcConnector;
import si.module.prtg.client.xmlrpc.commands.sensor.Sensor;
import si.module.prtg.client.xmlrpc.commands.version.Version;

public class CommandLineGenerator
{
	public CommandLineGenerator()
	{

	}

	public void Run()
	{
		try
		{
			Terminal T = TerminalBuilder.builder().system(true).streams(System.in, System.out).build();
			
			LineReader LR = new LineReaderImpl(T);

			LR.printAbove("#####################");
			LR.printAbove("# PRTG Client SF 10 #");
			LR.printAbove("#####################");
			
			String IPorDNS = LR.readLine("STARFACE IP/DNS:");
			String Instancename = LR.readLine("Instancename:");
			String Password = LR.readLine("Modulepassword:");
			String Sensorname = LR.readLine("Sensorname:");
			String SUseSSL = LR.readLine("Use SSL? (y/n):");
			Boolean UseSSL = SUseSSL.equalsIgnoreCase("y");

			Integer Port = 0;
			if(UseSSL)
			{
				Port=443;
			}
			else
			{
				Port=80;
			}
			
			String SPort= LR.readLine("Port (Default if left empty: "+Port+"):");
			if(!SPort.isEmpty())
			{
				Port = Integer.parseInt(SPort);
			}
				
			LR.printAbove("Testing Connection...");
			
			XmlRpcConnector XMLRPC = new XmlRpcConnector(Instancename, IPorDNS, Password, UseSSL, true);
			Version V = new Version();
			XMLRPC.execute(V);
			Result Result = V.Result();
			LR.printAbove("#####################");
			if(Result.Success())
			{
				LR.printAbove("OK!");
				LR.printAbove(Result.Data().toString());
				LR.printAbove("#####################");
				LR.printAbove("Testing Sensor...");
				Sensor S = new Sensor(Sensorname);
				XMLRPC.execute(S);
				
				Result = S.Result();
				if(Result.Success())
				{
					LR.printAbove("OK!");
					LR.printAbove(Result.Data().toString());
				}
				else
				{
					LR.printAbove("ERROR!");
					LR.printAbove(Result.Errormessage());
				}
				LR.printAbove("#####################");
				
				LR.printAbove("Generating String...");
				LR.printAbove("---------------------");
				LR.printAbove("-h %host -t " + Password+" -i "+ Instancename +" -s " + Sensorname +" -ssl " + UseSSL +" -d false -usev10");
				LR.printAbove("---------------------");
			}
			else
			{
				LR.printAbove("ERROR!");
				LR.printAbove(Result.Errormessage());
			}
			LR.printAbove("#####################");
			
			LR.readLine("Press any Key to exit...");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
