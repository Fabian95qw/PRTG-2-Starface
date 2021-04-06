package nucom.module.prtg.client.xmlrpc.commands.version;

import nucom.module.prtg.client.utility.EnumHelper.Commands;
import nucom.module.prtg.client.xmlrpc.commands.BasicCommand;

public class Version extends BasicCommand
{

	public Version() 
	{
		super(Commands.version, null);
	}

}
