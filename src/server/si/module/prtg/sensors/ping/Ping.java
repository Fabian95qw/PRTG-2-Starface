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

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class Ping implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="IPorDNS", description="", type=VariableType.STRING)
	public String IPorDNS="";
	
	@InputVar(label="Timeout", description="",type=VariableType.NUMBER)
	public Integer Timeout=5;
	
	@InputVar(label="PacketsperInterval", description="",type=VariableType.NUMBER)
	public Integer PacketsperInterVal=20;

	@OutputVar(label="MaxPing", description="",type=VariableType.NUMBER)
	public Integer MaxPing=-1;
	
	@OutputVar(label="MinPing", description="",type=VariableType.NUMBER)
	public Integer MinPing=-1;
	
	@OutputVar(label="AvgPing", description="",type=VariableType.NUMBER)
	public Integer AvgPing=-1;
	
	@OutputVar(label="PaketLossPercent", description="",type=VariableType.NUMBER)
	public Integer PacketLossPercent=-1;
	
	@OutputVar(label="HasTimeout", description="",type=VariableType.BOOLEAN)
	public Boolean hasTimeout=false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.debug("HasTimeout: " + hasTimeout);
	
		Execute4 E = new Execute4();
		E.executeAs = "Shell Command";
		String Command = "ping "+ IPorDNS+ " -c "+ PacketsperInterVal +" -W " + Timeout +" -q | grep -e \"Name or Service not known\" -e \"packet loss\" -e \"rtt\""; 
		E.command = Command;
		E.execute(context);
		
		String Result = E.output;
		log.debug(Result);
		log.debug(E.errorOutput);
		log.debug(E.resultCode);
		
		
		if(Result.contains("Name or service not known"))
		{
			hasTimeout=true;
		}
		else
		{
			String[] Lines = Result.split(System.lineSeparator());
			//########################################
			String[] Line1 = Lines[0].split(",");
			String Loss = Line1[2];
			Loss =Loss.replaceAll("[^\\d.]", "");
			PacketLossPercent = Integer.valueOf(Loss);
			
			//########################################
			String[] Line2 = Lines[1].split("=");
			//########################################
			String[] Results = Line2[1].replaceAll(" ","").split("/");

			log.debug("#####################");
			for(String S : Results)
			{
				log.debug(S);
			}
			//########################################
			String SMinPing = Results[0].replaceAll("\\.","");
			String SAvgPing = Results[1].replaceAll("\\.","");
			String SMaxPing = Results[2].replaceAll("\\.","");
						
			if(!SMinPing.isEmpty())
			{
				MinPing = Integer.valueOf(SMinPing)/1000;
			}
			if(!SAvgPing.isEmpty())
			{
				AvgPing = Integer.valueOf(SAvgPing)/1000;
			}
			
			if(!SMaxPing.isEmpty())
			{
				MaxPing = Integer.valueOf(SMaxPing)/1000;
			}	
			
			log.debug("Min:" + MinPing);
			log.debug("Avg: " + AvgPing);
			log.debug("Max: " + MaxPing);
		}

		
		/*
		Pings.addAll(PI.getPingResults());
		
		while(Pings.size() > NumberofPackets)
		{
			Pings.remove(0);
		}
		
		Double DAvg = (Pings.stream().mapToInt(I -> I).average().orElse(-1.0));
		AvgPing = DAvg.intValue();
		MaxPing = Pings.stream().mapToInt(I -> I).max().orElse(-1);
		MinPing = Pings.stream().mapToInt(I -> I).min().orElse(-1);
		
		PingStorage.Pings.put(IPorDNS, Pings);
		log.debug("Total Stored Pings:" + Pings.size());
		*/
		
	}//END OF EXECUTION


	
	
}
