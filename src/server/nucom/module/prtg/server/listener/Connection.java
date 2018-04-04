package nucom.module.prtg.server.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.SSLSocket;

import org.apache.commons.logging.Log;

import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import nucom.module.prtg.server.utility.LogHelper;
import nucom.module.prtg.server.utility.Storage;
import nucom.module.prtg.server.utility.EnumHelper.Unit;
import nucom.module.prtg.server.xml.XMLConstructor;

public class Connection implements Runnable
{
	private BufferedReader BIS = null;
	private OutputStream Out = null;
	private SSLSocket S = null;
	private String Password =null;
	private Log log = null;

	public Connection(SSLSocket S, String Password,  IRuntimeEnvironment context)
	{
		this.Password=Password;
		this.log=context.getLog();
		this.S=S;

	}

	@Override
	public void run() 
	{
		try
		{			
			log.debug("[C] Doing Handshake");
			//Do the initial Handshake, and get the Streams
			S.startHandshake();
			BIS = new BufferedReader(new InputStreamReader(S.getInputStream()));
			Out = S.getOutputStream();			
			
			log.debug("[C] Handshake Sucessful");
		}
		catch(Exception e)
		{
			log.debug("[C] There was an Error in the initial Handshake");
			log.debug(e);
			Close();
			return;
		}
		
		String Line ="";
		try
		{
			//Waiting for the Client to write the Password
			Line = BIS.readLine();
			log.debug("[C] Waiting for Client");
			//Check if Password is correct
			if(Line.equals(Password))
			{
				log.debug("[C] Client login was sucessful");
				if(Storage.XMLC == null)
				{
					//If currently no valid package exists, return the default package with the login successful channel
					XMLConstructor XMLC = new XMLConstructor(log);
					XMLC.AddResult("Login", "1", Unit.Custom, "Success", null);
					Out.write(XMLC.toString().getBytes());

				}
				else
				{
					//Add/Update the Login channel with a successful == 1
					Storage.XMLC.AddResult("Login", "1", Unit.Custom, "Success", null);
					Out.write(Storage.XMLC.toString().getBytes());
				}
			}
			else
			{
				//Return an XML with a Successful == 0, meaning the login has failed
				log.debug("[C] Client login failed. Provided Password:" + Line);
				XMLConstructor XMLC = new XMLConstructor(log);
				XMLC.AddResult("Login", "0", Unit.Custom , "Success", null);
				Out.write(XMLC.toString().getBytes());
			}
		}
		catch(IOException e)
		{
			log.debug("[C] Error while exchanging PRTG XML");
			LogHelper.EtoStringLog(log, e);
		}
		
		log.debug("[C]Closing Connection " + S.getRemoteSocketAddress().toString());
			

		Close();
		
	}
	
	private void Close()
	{
		try
		{
			Out.close();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		
		try
		{
			BIS.close();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
	}
}
