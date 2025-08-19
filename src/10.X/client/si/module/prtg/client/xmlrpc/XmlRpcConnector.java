package si.module.prtg.client.xmlrpc;

import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

import si.module.prtg.client.utility.Log;
import si.module.prtg.client.utility.MessageLoggingTransport;
import si.module.prtg.client.xmlrpc.commands.BasicCommand;

public class XmlRpcConnector
{
	private String Instancename = "";
	private XmlRpcClient XPC = null;
	private XmlRpcClientConfigImpl Config  = null;
	private Log log = null;

	public XmlRpcConnector(String Instancename, String IPorDNS, String Token, boolean UseSSL) throws MalformedURLException
	{
		log=new Log(this.getClass());
		String Url="";
		if(UseSSL)
		{
			Url ="https://"+IPorDNS+"/xml-rpc";
			trustEveryone();
		}
		else
		{
			Url ="http://"+IPorDNS+"/xml-rpc";
		}
			
		this.Instancename=Instancename;
		Config = new XmlRpcClientConfigImpl();
		
		Url = Url+"?de.vertico.starface.auth="+Token;
		//System.out.println(Url);
		log.debug(Url);

	
		Config.setServerURL(new java.net.URL(Url));
		
		XPC = new XmlRpcClient();
		
		final XmlRpcTransportFactory transportFactory = new XmlRpcTransportFactory()
		{
		    public XmlRpcTransport getTransport()
		    {
		        return new MessageLoggingTransport(XPC);
		    }
		};
		
		XPC.setTransportFactory(transportFactory);
				
		XPC.setConfig(Config);
	}
		
	private Result execute(Map<String, Object> Params) throws XmlRpcException
	{
		log.debug("Executing Command...");
		return Result.fromObject(XPC.execute(Instancename+"."+"interface", new Object[] {Params} ));
	}
	
	public void execute(BasicCommand BC) throws Exception
	{
		Map<String, Object> Mapping = BC.getMapping();
		BC.setResult(this.execute(Mapping));
	}
	
	private void trustEveryone() { 
	    try { 
	            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){ 
	                    public boolean verify(String hostname, SSLSession session) { 
	                            return true; 
	                    }}); 
	            SSLContext context = SSLContext.getInstance("TLS"); 
	            context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
	                    public void checkClientTrusted(X509Certificate[] chain, 
	                                    String authType) throws CertificateException {} 
	                    public void checkServerTrusted(X509Certificate[] chain, 
	                                    String authType) throws CertificateException {} 
	                    public X509Certificate[] getAcceptedIssuers() { 
	                            return new X509Certificate[0]; 
	                    }}}, new SecureRandom()); 
	            HttpsURLConnection.setDefaultSSLSocketFactory( 
	                            context.getSocketFactory()); 
	    } catch (Exception e) { // should never happen 
	            e.printStackTrace(); 
	    } 
	} 
		
}
