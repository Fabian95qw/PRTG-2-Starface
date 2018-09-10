package nucom.module.prtg.server.entrypoints;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import nucom.module.prtg.server.listener.ConnectionListener;
import nucom.module.prtg.server.manager.StorageManager;
import nucom.module.prtg.server.utility.LogHelper;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Starting a PRTG Listener, will do nothing if Port is already open.")
public class StartPRTGListener implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	
	
	@InputVar(label="Port", description="Port to Open for the PRTG Listener. IMPORTANT you still need to open the Port in the IPTables",type=VariableType.NUMBER)
	public Integer Port=25590;
		    	
	@InputVar(label="Password", description="Set Password, which is Required for the Listener to Accept the Connection",type=VariableType.STRING)
	public String Password="";
	
	@InputVar(label="KeyStoreLocation", description="KeyStore used to create the TLS-Connection.",type=VariableType.STRING)
	public String KeyStoreLocation="/usr/share/tomcat6/ssl/tomcat.keystore";
	
	@InputVar(label="KeyStorePassword", description="Password for the KeyStore",type=VariableType.STRING)
	public String KeyStorePassword="";
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		//Checking if there is already a Connection Listener existing, and still active
		
		StorageManager SM = (StorageManager)context.provider().fetch(StorageManager.class);
		
		if(!SM.isRunning())
		{
			log.debug("Starting StorageManager");
			try 
			{
				SM.startComponent();
			} 
			catch (Throwable e) 
			{
				LogHelper.EtoStringLog(log, e);
			}
		}
		
		if(SM.getComponentProvider() == null || !SM.getConnectionListener().isRunning() )
		{
			//Creating a New Listener
			log.debug("Trying to Start the PRTG Listener");
			ConnectionListener CL = new ConnectionListener(Port, Password, KeyStoreLocation, KeyStorePassword, context);
			SM.setConnectionListener(CL);
			//Starting a new Thread for the Listener
			Thread T = new Thread(CL);
			T.start();			
		}
		else
		{
			log.debug("Connection Listener is already active");
		}
		
		
	}//END OF EXECUTION

	
}
