package nucom.module.prtg.client.xmlrpc;

import java.net.MalformedURLException;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

import nucom.module.prtg.client.utility.Log;
import nucom.module.prtg.client.utility.MessageLoggingTransport;
import nucom.module.prtg.client.xmlrpc.commands.BasicCommand;

public class XmlRpcConnector
{
	private String Instancename = "";
	private XmlRpcClient XPC = null;
	private XmlRpcClientConfigImpl Config  = null;
	private Log log = null;
	
	public XmlRpcConnector(String Instancename, String IPorDNS, String Token) throws MalformedURLException
	{
		log=new Log(this.getClass());
		String Url ="http://"+IPorDNS+"/xml-rpc";

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
		
}
