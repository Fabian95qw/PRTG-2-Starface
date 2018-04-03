package nucom.module.prtg.client.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger 
{
	private String Name ="";
	private File LogFile = null;
	private FileWriter FW = null;
	private SimpleDateFormat SDF = new SimpleDateFormat("[dd/MM/yy HH:mm:ss]");
	private boolean Log = true;
	
	public Logger(String Name, String LogLocation)
	{
		//Creating a new instance of the logger

		//If no Loglocation was Supplied, don't log
		if(LogLocation.isEmpty())
		{
			Log = false;
			return;
		}
		
		this.Name=Name;
		
		//Try to create a logfile at the given location
		LogFile = new File (LogLocation+Name+".log");
		
		try 
		{
			FW = new FileWriter(LogFile);
		} 
		catch (IOException e) 
		{
			//If something goes wrong, print the stacktrace, and disable further logging attempts for logger
			
			//Note: This output will cause a malformed XML exception in prtg monitor
			e.printStackTrace();
			Log=false;
		}
	}
	
	public void log(String S)
	{
		if(!Log) {return;}
		try 
		{
			String Temp = "["+Name+"]"+SDF.format(new Date()) +" " + S;
			FW.write(Temp);
			FW.write(System.lineSeparator());
			FW.flush();
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
		if(!Log) {return;}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log(sw.toString());
	}
	
	
}
