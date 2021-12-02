package si.module.prtg.client.xmlrpc.commands;

import java.util.HashMap;
import java.util.Map;

import si.module.prtg.client.utility.EnumHelper.Commands;
import si.module.prtg.client.xmlrpc.Result;
import si.module.prtg.client.xmlrpc.XmlRpcConnector;

public abstract class BasicCommand 
{
	protected Map<String, Object> Mapping = null;
	protected XmlRpcConnector XPC = null;
	protected Result Result=null;
		
	public BasicCommand(Commands Command, Map<?, ?> Data)
	{
		if(Data == null)
		{
			Data= new HashMap<Object, Object>();
		}
		this.Mapping= new HashMap<String, Object>();
		//System.out.println(Command.toString());
		//System.out.println(Data.toString());
		
		
		Mapping.put("Command", Command.toString());
		Mapping.put("Params", Data);
	}
	
	//public void execute(XmlRpcConnector XPC) throws Exception
	//{
	//	Result = XPC.execute(Mapping);
	//}
		
	public Map<String, Object> getMapping()
	{
		return this.Mapping;
	}
	
	public void setResult (Result R)
	{
		this.Result=R;
	}
	
	public Result Result()
	{
		return this.Result;
	}
}
