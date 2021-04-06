package nucom.module.prtg.server.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.logging.Log;

import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import nucom.module.prtg.server.utility.LogHelper;

public class ConnectionListener implements Runnable
{
	private IRuntimeEnvironment context = null;
	private Log log = null;
	private Integer Port = -1;
	private String Password=null;
	private File KeyStoreLocation = null;
	private String KeyStorePassword=null;
	private boolean isRunning = false;
	private SSLServerSocket S  = null;
	
	public ConnectionListener(Integer Port, String Password, String KeyStoreLocation, String KeyStorePassword, IRuntimeEnvironment context)
	{
		this.context=context;
		this.log=context.getLog();
		this.Port=Port;
		this.Password=Password;
		if(Password.isEmpty())
		{
			this.Password="";
		}
		else
		{
			this.Password=Encrypt(Password);
		}
				
		this.KeyStoreLocation=new File(KeyStoreLocation);
		this.KeyStorePassword=KeyStorePassword;
		log.debug("[CL] New Listener Created");
	}
	
	
	@Override
	public void run() 
	{
		log.debug("[CL] Loading KeyStore: " + KeyStoreLocation.getAbsolutePath());
		
		KeyStore KS = null;
		KeyManagerFactory KMF = null;
		TrustManagerFactory TMF = null;
		SSLContext SC = null;
		TrustManager[] TM = null;
		
		//Loading the Supplied KeyStore, by default the tomcat keystore of the starface pbx
		try 
		{
			KS = KeyStore.getInstance("JKS");
			KS.load(new FileInputStream((KeyStoreLocation)), KeyStorePassword.toCharArray());
			KMF = KeyManagerFactory.getInstance("SunX509");
			KMF.init(KS, KeyStorePassword.toCharArray());
			TMF =  TrustManagerFactory.getInstance("SunX509");
			TMF.init(KS);
			SC = SSLContext.getInstance("TLS");
			TM = TMF.getTrustManagers(); 
			SC.init(KMF.getKeyManagers(), TM, null); 
		} 
		catch (Exception e) 
		{
			log.debug("[CL]Error Occured during Access of the KeyStore");
			LogHelper.EtoStringLog(log, e);
			return;
		} 
		
		log.debug("[CL] Starting Listener on Port: " + Port);
		
		SSLServerSocketFactory SocketFactory = SC.getServerSocketFactory();
		
		S = null;

		isRunning = true;
		//Create the SSL Socket factory
		try 
		{
			S = (SSLServerSocket) SocketFactory.createServerSocket(Port);
			log.debug("[CL]Waiting for Connection");
			while(isRunning)
			{
				SSLSocket Client = null;
				Client  = (SSLSocket) S.accept();
				log.debug("[CL]New Connection from: " +Client.getInetAddress().toString());
				//Put every connection in it's own thread
				Connection C = new Connection(Client, Password, context);
				Thread T = new Thread(C);
				T.start();				
				
			}
		} 
		catch (IOException e) 
		{
			LogHelper.EtoStringLog(log, e);
		}
		
		isRunning = false;
		
	}

	public boolean isRunning() 
	{
		return isRunning;
	}


	public void shutdown()
	{
		log.debug("[CL] Closing Socket");
		this.isRunning = false;
		try
		{
			S.close();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		
	}

	private String Encrypt(String Password)
	{
		//Thank you https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
		byte[] Salt = "*Starface*".getBytes();
		byte[] Passwordhash = null;
		KeySpec KS = new PBEKeySpec(Password.toCharArray(), Salt, 65536, 128);
		SecretKeyFactory SKF = null;
		Encoder ENC = Base64.getEncoder();		
		
		log.debug("Encrypting Password");
		
		try 
		{
			SKF = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			log.debug(e);
			return null;
		}
		
		try 
		{
			Passwordhash = SKF.generateSecret(KS).getEncoded();
		}
		catch (InvalidKeySpecException e) 
		{
			log.debug(e);
			return null;
		}
		
		return ENC.encodeToString(Passwordhash);
	}
	
	
}
