package si.module.prtg.client.xmlrpc.commands.version;

import si.module.prtg.client.utility.EnumHelper.Commands;
import si.module.prtg.client.xmlrpc.commands.BasicCommand;

public class Version extends BasicCommand
{

	public Version() 
	{
		super(Commands.version, null);
	}

}
