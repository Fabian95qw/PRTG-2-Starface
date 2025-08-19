package si.module.prtg.sensors.ping;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.module.core.runtime.functions.system.Execute4;

@Function(visibility = Visibility.Private, rookieFunction = false, description = "")
public class Ping implements IBaseExecutable
{
	// ##########################################################################################

	@InputVar(label = "IPorDNS", description = "", type = VariableType.STRING)
	public String IPorDNS = "";

	@InputVar(label = "Timeout", description = "", type = VariableType.NUMBER)
	public Integer Timeout = 5;

	@InputVar(label = "PacketsperInterval", description = "", type = VariableType.NUMBER)
	public Integer PacketsperInterVal = 20;

	@OutputVar(label = "MaxPing", description = "", type = VariableType.NUMBER)
	public Integer MaxPing = -1;

	@OutputVar(label = "MinPing", description = "", type = VariableType.NUMBER)
	public Integer MinPing = -1;

	@OutputVar(label = "AvgPing", description = "", type = VariableType.NUMBER)
	public Integer AvgPing = -1;

	@OutputVar(label = "PaketLossPercent", description = "", type = VariableType.NUMBER)
	public Integer PacketLossPercent = -1;

	@OutputVar(label = "HasTimeout", description = "", type = VariableType.BOOLEAN)
	public Boolean hasTimeout = false;

	StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance();
	// ##########################################################################################

	// ################### Code Execution ############################
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Logger log = context.getLog();

		log.debug("HasTimeout: " + hasTimeout);

		Execute4 E = new Execute4();
		E.executeAs = "Shell Command";
		String Command = "ping " + IPorDNS + " -c " + PacketsperInterVal + " -W " + Timeout
				+ " -q | grep -e \"Name or Service not known\" -e \"packet loss\" -e \"rtt\"";
		E.command = Command;
		E.execute(context);

		String Output = E.output;
		log.trace(Output);
		log.trace(E.errorOutput);
		log.trace(E.resultCode);

		if (Output.contains("Name or service not known"))
		{
			hasTimeout = true;
			PacketLossPercent = 100;
		}
		else
		{
			String[] Lines = Output.split(System.lineSeparator());
			log.trace("Lines: " + Lines.length);
			if (Lines.length < 2)
			{
				PacketLossPercent = 100;
				hasTimeout = true;
				log.error("Error while determining Packet loss");
			}
			else
			{
				// ########################################
				String[] Line1 = Lines[0].split(",");
				log.trace("Line1: " + Line1.length);
				if (Line1.length < 3)
				{
					PacketLossPercent = 100;
					hasTimeout = true;
					log.error("Error while reading Packet loss");

				}
				else
				{
					String Loss = Line1[2];
					Loss = Loss.replaceAll("[^\\d.]", "");
					PacketLossPercent = Integer.valueOf(Loss);
				}
				// ########################################
				String[] Line2 = Lines[1].split("=");
				// ########################################
				log.trace("Line2: " + Line2.length);
				if (Line2.length < 2)
				{
					MinPing = -1;
					AvgPing = -1;
					MaxPing = -1;
					hasTimeout = true;
					log.error("Error while determining Ping Times");
				}
				else
				{
					String[] Results = Line2[1].replaceAll(" ", "").split("/");

					if (Results.length < 3)
					{
						log.error("Error while reading Ping Times");
					}
					else
					{
						log.trace("#####################");
						for (String S : Results)
						{
						log.trace(S);
						}
						// ########################################
						String SMinPing = Results[0].replaceAll("\\.", "");
						String SAvgPing = Results[1].replaceAll("\\.", "");
						String SMaxPing = Results[2].replaceAll("\\.", "");

						if (!SMinPing.isEmpty())
						{
							MinPing = Integer.valueOf(SMinPing) / 1000;
							if(MinPing == 0)
							{
								MinPing=1;
							}
						}
						if (!SAvgPing.isEmpty())
						{
							AvgPing = Integer.valueOf(SAvgPing) / 1000;
							if(AvgPing == 0)
							{
								AvgPing=1;
							}
						}

						if (!SMaxPing.isEmpty())
						{
							MaxPing = Integer.valueOf(SMaxPing) / 1000;
							if(MaxPing == 0)
							{
								MaxPing=1;
							}
						}
					}


				}

				log.trace("Min:" + MinPing);
				log.trace("Avg: " + AvgPing);
				log.trace("Max: " + MaxPing);
			}
		}

	}// END OF EXECUTION

}
