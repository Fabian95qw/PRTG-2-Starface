package nucom.module.prtg.client.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger 
{
	private String Name ="";
	private SimpleDateFormat SDF = new SimpleDateFormat("[dd/MM/yy HH:mm:ss]");

	//WARNING this will cause a malformed XML-Exception in the PRTG Monitor! FOR DEBUG USE ONLY
	//If Debug is enabled, do direct output to the Console.
	private boolean Debug=false;
	
	public Logger(String Name, Boolean Debug)
	{
		//Creating a new instance of the logger
		this.Debug=Debug;
		this.Name=Name;
	}
	
	public void log(String S)
	{
		if(!Debug) {return;}
		try 
		{
			String Temp = "["+Name+"]"+SDF.format(new Date()) +" " + S;
			System.out.println(Temp);
		} 
		catch (Exception e) 
		{
			//Note: This output will cause a malformed XML exception in prtg monitor
			e.printStackTrace();
		}
	}
	
	public void log(Exception e)
	{
		//Exception to String for the Logger
		if(!Debug) {return;}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log(sw.toString());
	}
	
	
}
