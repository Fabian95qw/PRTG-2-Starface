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
	private Logger l =null;
	
	public Connection(String IPorDNS, Integer Port, String Password)
	{
		this.IPorDNS=IPorDNS;
		this.Port=Port;
		if(Password.isEmpty())
		{
			this.Password = null;
		}
		else
		{
			this.Password=Password;
		}
		l = new Logger("Connection", EntryPoint.CM.S(Config.LogLocation));
	}
	
	public void Open()
	{
		SSLSocketFactory  SocketFactory = null;
		
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
			}
			
			try 
			{
				SC.init(null , trustAllCerts , new SecureRandom());
			}
			catch (KeyManagementException e) 
			{
				l.log("SSL Context generation error");
				l.log(e);
			}
			
			SocketFactory = SC.getSocketFactory(); //(SSLSocketFactory)SSLSocketFactory.getDefault();
		}
		else
		{
			SocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		}
		
		try 
		{
			S = (SSLSocket)SocketFactory.createSocket(IPorDNS, Port);
						
			BIS = new BufferedReader(new InputStreamReader(S.getInputStream()));
			Out = S.getOutputStream();
			
			l.log("Starting Handshake");
			
			S.startHandshake();
			
			l.log("HandShake Completed");
			
			l.log("Writing Password");
			
			Out.write(Password.getBytes());
			Out.write(System.lineSeparator().getBytes());

			String Line ="";
			Line = BIS.readLine();
			l.log(Line);
			
			System.out.println(Line);
			
			S.close();
		} 
		catch (UnknownHostException e) 
		{
			l.log("Connection Failed.");
			l.log(e);
		} 
		catch (IOException e) 
		{
			l.log("Connection Failed.");
			l.log(e);
		}
		Close();
	}
	
	private void Close()
	{
		try
		{
			Out.close();
			BIS.close();
			S.close();
		}
		catch(Exception e)
		{
			l.log(e);
		}
	}
	
	private static final TrustManager[] trustAllCerts = new TrustManager[] 
	{
			//TMF =  TrustManagerFactory.getInstance("SunX509");
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
		
}
