package si.module.prtg.sensors.calls;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import de.starface.bo.callhandling.actions.ModuleBusinessObject;
import de.starface.callhandling.Callhandling;
import de.starface.callhandling.asteriskmodel.api.AsteriskModelApi;
import de.starface.ch.processing.bo.api.pojo.data.PojoCall;
import de.starface.ch.processing.bo.api.pojo.data.PojoCallLeg;

import de.vertico.starface.config.wire.forms.WireUnitBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IAGIJavaExecutable;
import de.vertico.starface.module.core.runtime.IAGIRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.WireSettingsHandler;

@Function(visibility = Visibility.Private,  description = "")
public class CountCallChannels implements IAGIJavaExecutable
{
	// ##########################################################################################

	@OutputVar(label = "Total", description = "", type = VariableType.NUMBER)
	public Integer Total = 0;

	@OutputVar(label = "TotalIntern", description = "", type = VariableType.NUMBER)
	public Integer TotalIntern = 0;

	@OutputVar(label = "TotalExtern", description = "", type = VariableType.NUMBER)
	public Integer TotalExtern = 0;

	@OutputVar(label = "TotalPerLine", description = "", type = VariableType.MAP)
	public Map<String, Integer> TotalPerLine = new HashMap<String, Integer>();

	
	// ##########################################################################################

	// ################### Code Execution ############################
	@Override
	public void execute(IAGIRuntimeEnvironment context) throws Exception
	{
		Logger log = context.getLog();

		ModuleBusinessObject MBO = (ModuleBusinessObject) context.springApplicationContext().getBean(ModuleBusinessObject.class);

		WireSettingsHandler WSH = (WireSettingsHandler) context.springApplicationContext().getBean(WireSettingsHandler.class);

		Collection<WireUnitBean> Lines = WSH.getProviderConnections();

		Map<String, String> NameMapping = new HashMap<String, String>();

		for (WireUnitBean Line : Lines)
		{
			TotalPerLine.put(Line.getWireName(), 0);
			NameMapping.put(Line.getProviderBean().getUserName(), Line.getWireName());
		}

		PojoCall C = null;

		Callhandling CH = context.springApplicationContext().getBean(Callhandling.class);

		AsteriskModelApi AMA = CH.getAsteriskModelApi();

		List<UUID> IgnoreCallIDs = new ArrayList<UUID>();

		for (String S : AMA.getAllChannelNames())
		{
			try
			{
				C = MBO.getPojoCallByChannelName(S);
				if (IgnoreCallIDs.contains(C.getCallId()))
				{
					continue;
				}
				else
				{
					IgnoreCallIDs.add(C.getCallId());
				}

				for (PojoCallLeg PCL : C.getAllCallLegs())
				{
					log.debug("###########################");
					log.debug(PCL.toString());
					if (PCL.getAccountId() > 0)
					{
						log.debug("Internal Call");
						TotalIntern = TotalIntern + 1;
					}
					else
					{
						log.debug("Checking if External..");
						switch (PCL.getPeerType())
						{
						case FAX:
						case FMC:
						case LOCALPHONE:
						case PHONE:
						case PROXY_PHONE:
							TotalIntern = TotalIntern + 1;
							log.debug("Internal Call");
							break;
						case UNKNOWN:
							log.debug("External Call");
							TotalExtern = TotalExtern + 1;
							String LineName = "Unbekannt";

							try
							{
								String[] Pieces = PCL.getPeerName().split("/");
								if (NameMapping.get(Pieces[1]) != null)
								{
									LineName = NameMapping.get(Pieces[1]);
								}
							}
							catch (Exception e)
							{
								EtoStringLog(log, e);
							}

							if (TotalPerLine.containsKey(LineName))
							{
								TotalPerLine.put(LineName, TotalPerLine.get(LineName) + 1);
							}
							break;
						}
					}
				}
			}
			catch (Exception e)
			{
				EtoStringLog(log, e);
			}
		}

		Total = TotalIntern + TotalExtern;
		
		log.debug("Total:" + Total);
		log.debug("TotalExtern:" + TotalExtern);
		log.debug("TotalIntern:" + TotalIntern);
		log.debug("Total Leitungen:" +TotalPerLine.toString());
		
	}// END OF EXECUTION

	public static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}

}
