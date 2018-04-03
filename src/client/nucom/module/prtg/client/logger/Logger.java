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

		this.Name=Name;
				
		LogFile = new File (LogLocation+Name+".log");

		if(LogLocation.isEmpty())
		{
			Log = false;
			return;
		}
		
		try 
		{
			FW = new FileWriter(LogFile);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	public void log(Exception e)
	{
		if(!Log) {return;}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log(sw.toString());
	}
	
	
}
