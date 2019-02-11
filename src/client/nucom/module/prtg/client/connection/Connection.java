package nucom.module.prtg.client.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import nucom.module.prtg.client.logger.Logger;
import nucom.module.prtg.client.EntryPoint;
import nucom.module.prtg.client.config.EnumHelper.Config;

public class Connection 
{
	private String IPorDNS ="";
	private Integer Port =-1;
	private SSLSocket S = null;
	private BufferedReader BIS = null;
	private OutputStream Out = null;
	private String Password = null;
	private String Sensor =null;
	private Logger l =null;
	
	public Connection(String IPorDNS, Integer Port, String Password, String Sensor)
	{
		this.IPorDNS=IPorDNS;
		this.Port=Port;
		this.Sensor=Sensor;
		l = new Logger("Connection", EntryPoint.CM.B(Config.Debug));
		if(Password.isEmpty())
		{
			this.Password = "";
		}
		else
		{
			this.Password=Encrypt(Password);
		}

	}
	
	public void Open()
	{
		SSLSocketFactory  SocketFactory = null;
		
		//When TrustallCA is true, create a trustmanager, to trust all ca's
		//Note to self: HIGHLY DISCOURAGED! Only use for debugging purpose
		if(EntryPoint.CM.B(Config.TrustallCA))
		{
			SSLContext SC = null; 
			try 
			{
				SC = SSLContext.getInstance("TLS");
			} 
			catch (NoSuchAlgorithmException e) 
			{
				l.log("Context generation Failed");
				l.log(e);
				return;
			}
			
			try 
			{
				SC.init(null , trustAllCA , new SecureRandom());
			}
			catch (KeyManagementException e) 
			{
				l.log("SSL Context generation error");
				l.log(e);
				return;
			}
			
			SocketFactory = SC.getSocketFactory();
		}
		else
		{
			//Create a system default socket factory
			SocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		}
		
		try 
		{
			//Open connection to server
			S = (SSLSocket)SocketFactory.createSocket(IPorDNS, Port);
						
			BIS = new BufferedReader(new InputStreamReader(S.getInputStream()));
			Out = S.getOutputStream();
			
			l.log("Starting Handshake");
			
			S.startHandshake();
			
			l.log("HandShake Completed");
			
			l.log("Writing Password");
			
			//After the Handshake, write out the encrypted password
						
			Out.write(Password.getBytes());
			Out.write(System.lineSeparator().getBytes());

			l.log("Writing Sensor to Access");
			
			Out.write(Sensor.getBytes());
			Out.write(System.lineSeparator().getBytes());
			
			l.log("Waiting for Server Response");
			
			//Waiting for a response, the server will return a response in both cases, wherever login was sucessful or not.
			
			Thread.sleep(1000);
			
			String Line ="";
			Line = BIS.readLine();
			
			//Write an empty line back, to inform the server about closing the connection
			Out.write(System.lineSeparator().getBytes());
			
			//Log XML in the Logfile, and in the console, for the PRTG-Monitor to read.
			l.log(Line);
			
			System.out.println(Line);
			
			S.close();
		} 
		catch (UnknownHostException e) 
		{
			//If host is unknown, log it
			l.log("Connection Failed.");
			l.log(e);
		} 
		catch (IOException e) 
		{
			//If anything goes wrong, log it
			l.log("Connection Failed.");
			l.log(e);
		} catch (InterruptedException e) {}
		Close();
	}
	
	private void Close()
	{
		//Close Streams individually, and log any exceptions
		try
		{
			Out.close();
		}
		catch(Exception e)
		{
			l.log(e);
		}
		try
		{
			BIS.close();
		}
		catch(Exception e)
		{
			l.log(e);
		}
		try
		{
			S.close();
		}
		catch(Exception e)
		{
			l.log(e);
		}
	}
	
	private static final TrustManager[] trustAllCA = new TrustManager[] 
	{
			//Define a trustamanager, whcih trusts every cert
			new X509TrustManager()
			{
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException 
				{
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException 
				{
					
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() 
				{
					return new X509Certificate[] {};
				}
				
			}
			
	};
	
	private String Encrypt(String Password)
	{
		//Thank you https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
		byte[] Salt = "*Starface*".getBytes();
		byte[] Passwordhash = null;
		KeySpec KS = new PBEKeySpec(Password.toCharArray(), Salt, 65536, 128);
		SecretKeyFactory SKF = null;
		Encoder ENC = Base64.getEncoder();		
		
		l.log("Encrypting Password");
		
		try 
		{
			SKF = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			l.log(e);
			return null;
		}
		
		try 
		{
			Passwordhash = SKF.generateSecret(KS).getEncoded();
		}
		catch (InvalidKeySpecException e) 
		{
			l.log(e);
			return null;
		}
		
		return ENC.encodeToString(Passwordhash);
	}
		
}
