package nucom.module.prtg.client.utility;

public class EnumHelper 
{
	public enum CoreConfig
	{
		Clients
	}

	public enum Commands
	{
		version,
		sensor
	}

	public enum CSVMode
	{
		NONE,
		MAP,
		LISTOFMAPS
	}
	
	public enum ResImage
	{
		Trying("/nucom/module/prtg/client/gui/img/trying.gif"),
		OK("/nucom/module/prtg/client/gui/img/ok.png"),
		Error("/nucom/module/prtg/client/gui/img/error.png"),
		;

		private String ReadableName;
		
		public String getValue() 
		{
		        return ReadableName;
		}
		
		ResImage(String value) 
		{
		        this.ReadableName = value;
		}
	}
	
}
