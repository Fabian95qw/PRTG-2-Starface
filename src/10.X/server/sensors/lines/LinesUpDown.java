package si.module.prtg.sensors.lines;

import java.util.Collection;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.config.wire.forms.WireUnitBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.module.core.runtime.functions.system.Execute4;
import de.vertico.starface.persistence.connector.WireSettingsHandler;

@Function(visibility = Visibility.Private,  description = "Returns the Amount of Lines currently Online, and Offline")
public class LinesUpDown implements IBaseExecutable
{
	// ##########################################################################################

	@OutputVar(label = "Lines Up", description = "", type = VariableType.NUMBER)
	public Integer LinesUp = 0;

	@OutputVar(label = "Lines Down", description = "", type = VariableType.NUMBER)
	public Integer LinesDown = 0;

	
	// ##########################################################################################

	// ################### Code Execution ############################
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		// CallActions CA = (CallActions)context.springApplicationContext().getBean(CallActions.class);

		Logger log = context.getLog();
		WireSettingsHandler WSH = (WireSettingsHandler) context.springApplicationContext().getBean(WireSettingsHandler.class);

		Collection<WireUnitBean> Lines = WSH.getProviderConnections();

		for (WireUnitBean Line : Lines)
		{
			log.debug("Line: " + Line.getWireName() + " ==> " + Line.getConnectionState());

			if (Line.getConnectionState().equals("on"))
			{
				LinesUp++;
			}
			else
			{
				log.debug("Manual SIP Check for: " + Line.getProviderBean().getHost() + " "
						+ Line.getProviderBean().getUserName());
				if (ManualSIPOnlineCheck(context, Line.getProviderBean().getHost(),
						Line.getProviderBean().getUserName())
						|| ManualSIPOnlineCheck(context, Line.getProviderBean().getUserName() + ":",
								Line.getProviderBean().getUserName())
						|| ManualSIPOnlineCheck(context, Line.getProviderBean().getUserName()))
				{
					LinesUp++;
				}
				else
				{
					LinesDown++;
				}
			}
		}

	}// END OF EXECUTION

	// Workaround for STARFACE 8.BUG
	private Boolean ManualSIPOnlineCheck(IRuntimeEnvironment context, String Value)
	{
		Logger log = context.getLog();
		Execute4 E = new Execute4();
		E.executeAs = "Asterisk CLI Command";
		E.command = "sip show registry fullusername";
		E.bufferSize = Integer.MAX_VALUE;
		try
		{
			E.execute(context);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			return false;
		}

		String[] Lines = E.output.split(System.lineSeparator());

		for (String Line : Lines)
		{
			log.trace(Line +" " +Line.contains(Value) +" " + Line.contains("Registered"));
			if (Line.contains(Value)&& Line.contains("Registered"))
			{
				return true;
			}
		}
		return false;
	}
	
	private Boolean ManualSIPOnlineCheck(IRuntimeEnvironment context, String Value1, String Value2)
	{
		Logger log = context.getLog();
		Execute4 E = new Execute4();
		E.executeAs = "Asterisk CLI Command";
		E.command = "sip show registry fullusername";
		E.bufferSize = Integer.MAX_VALUE;
		try
		{
			E.execute(context);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			return false;
		}

		String[] Lines = E.output.split(System.lineSeparator());

		for (String Line : Lines)
		{
			log.trace(Line +" " +Line.contains(Value1)+" "+Line.contains(Value1)  +" " + Line.contains("Registered"));
			if (Line.contains(Value1) && Line.contains(Value2) && Line.contains("Registered"))
			{
				return true;
			}
		}
		return false;
	}
}
