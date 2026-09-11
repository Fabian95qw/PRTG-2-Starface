package si.module.prtg.server.utility;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;

public class LogHelper 
{
	//Class to format Stacktraces to log-friendly Strings
	public static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
	public static void EtoStringLog(Logger log, Throwable e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
}
