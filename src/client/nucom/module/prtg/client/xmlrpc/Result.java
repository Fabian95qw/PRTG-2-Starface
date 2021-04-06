package nucom.module.prtg.client.xmlrpc;

import java.util.HashMap;
import java.util.Map;

public class Result 
{
	private Map<String, Object> Data = new HashMap<String, Object>();
	private boolean Success = false;
	private String Errormessage="";
	
	@SuppressWarnings("unchecked")
	private Result(Object O)
	{
		
		 Map<String, Object> ResultMap = (Map<String, Object>) O;
		 Data = (Map<String, Object>) ResultMap.get("Data");
		
		Success = (boolean) ResultMap.get("Success");
		Errormessage = (String) ResultMap.get("Errormessage");
				
	}
	
	public static Result fromObject(Object O)
	{
		return new Result(O);
	}
	
	
	public Map<String, Object> Data() {
		return Data;
	}

	public boolean Success() {
		return Success;
	}

	public String Errormessage() {
		return Errormessage;
	}
	
}
