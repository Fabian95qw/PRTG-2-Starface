package nucom.module.prtg.server.utility;

import nucom.module.prtg.server.listener.ConnectionListener;
import nucom.module.prtg.server.xml.XMLConstructor;

public class Storage 
{
	//In order to re-access the Objects in a new module-execution cycle, they need to be stored in some kind of static Object.
	public static ConnectionListener CL = null;
	public static XMLConstructor XMLC = null;
}
