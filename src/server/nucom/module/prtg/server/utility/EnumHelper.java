package nucom.module.prtg.server.utility;

public class EnumHelper 
{
	//Units taken from: Paesslers API: https://prtg.paessler.com/api.htm?username=demo&password=demodemo&tabid=7
	
	//Used for Module-GUI <==> Module execution Translation in "AddResultToPackage.class"
	public enum Unit
	{
		BytesBandwidth,
		 BytesMemory,
		 BytesDisk,
		 Temperature,
		 Percent,
		 TimeResponse,
		 TimeSeconds,
		 Custom,
		 Count,
		 BytesFile,
		 SpeedDisk,
		 SpeedNet,
		 TimeHours 
	}
}
