package nucom.module.prtg.client.config;

import java.io.Serializable;

public class ConnectionData implements Serializable
{

	private static final long serialVersionUID = -3208298448359215987L;
	private String IPorDNS;
	private String InstanceName;
	private String Token;
	
	public ConnectionData(String IPorDNS, String InstanceName, String Token)
	{
		this.IPorDNS=IPorDNS;
		this.InstanceName=InstanceName;
		this.Token=Token;

	}

	public String getInstanceName() {
		return InstanceName;
	}

	public void setInstanceName(String instanceName) {
		InstanceName = instanceName;
	}

	public String getIPorDNS() {
		return IPorDNS;
	}

	public void setIPorDNS(String iPorDNS) {
		IPorDNS = iPorDNS;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}
	
	
	
}
