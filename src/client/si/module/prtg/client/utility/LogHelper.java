package si.module.prtg.client.utility;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogHelper 
{
	public static void EtoStringLog(Log log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
	public static void EtoStringLog(Log log, Throwable e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
}
