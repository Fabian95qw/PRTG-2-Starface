package nucom.module.prtg.server.xml;

import java.util.Map;

import nucom.module.prtg.server.utility.EnumHelper.Unit;

public class Result 
{
	private String Channel ="";
	private String Value="";
	private Unit U = Unit.Custom;
	private String CustomUnit = "";
	private Map<String, String> Params = null;
	
	
	//Class used for the XML-Building
	public Result(String Channel, String Value, Unit U, String CustomUnit, Map<String, String> Params)
	{
		this.Channel=Channel;
		this.Value=Value;
		this.U=U;
		this.CustomUnit=CustomUnit;
		this.Params = Params;
	}

	public String getChannel() {
		return Channel;
	}

	public void setChannel(String channel) {
		Channel = channel;
	}

	public String getValue() {
		Value = Value.replaceAll("\\D+", "");
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public Unit getU() {
		return U;
	}

	public void setU(Unit u) {
		U = u;
	}

	public Map<String, String> getParams() {
		return Params;
	}

	public void setParams(Map<String, String> params) {
		Params = params;
	}

	public String getCustomUnit() {
		return CustomUnit;
	}

	public void setCustomUnit(String customUnit) {
		CustomUnit = customUnit;
	}
	
	
	
}
