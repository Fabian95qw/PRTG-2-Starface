package si.module.prtg.sensors.calls;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import de.starface.bo.callhandling.actions.ModuleBusinessObject;
import de.starface.callhandling.Callhandling;
import de.starface.callhandling.asteriskmodel.api.AsteriskModelApi;
import de.starface.callhandling.callmodel.enums.CallLegState;
import de.starface.ch.processing.bo.api.pojo.data.PojoCall;
import de.starface.ch.processing.bo.api.pojo.data.PojoCallLeg;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.config.wire.forms.WireUnitBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IAGIJavaExecutable;
import de.vertico.starface.module.core.runtime.IAGIRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.WireSettingsHandler;
import java.util.ArrayList;
import java.util.List;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class CountCallChannels implements IAGIJavaExecutable 
{
	//##########################################################################################
	
	@OutputVar(label="Total", description="",type=VariableType.NUMBER)
	public Integer Total=0;
	
	@OutputVar(label="TotalIntern", description="",type=VariableType.NUMBER)
	public Integer TotalIntern=0;
	
	@OutputVar(label="TotalExtern", description="",type=VariableType.NUMBER)
	public Integer TotalExtern=0;
	
	@OutputVar(label="TotalPerLine", description="",type=VariableType.MAP)
	public Map<String, Integer> TotalPerLine = new HashMap<String, Integer>();
	
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IAGIRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		ModuleBusinessObject MBO = (ModuleBusinessObject)context.provider().fetch(ModuleBusinessObject.class);
		
		WireSettingsHandler WSH = (WireSettingsHandler) context.provider().fetch(WireSettingsHandler.class);

		Collection<WireUnitBean> Lines = WSH.getProviderConnections();

		for (WireUnitBean Line : Lines)
		{
			TotalPerLine.put(Line.getWireName(), 0);
		}
		
		PojoCall C = null;
		
		Callhandling CH = context.provider().fetch(Callhandling.class);
		
		AsteriskModelApi AMA = CH.getAsteriskModelApi();
		List<UUID> IgnoreCallIDs = new ArrayList<UUID>();
				
		for(String S : AMA.getAllChannelNames())
		{
				C = MBO.getPojoCallByChannelName(S);	
				if(IgnoreCallIDs.contains(C.getCallId()))
				{
					continue;
				}
				else
				{
					IgnoreCallIDs.add(C.getCallId());
				}
				
				for(PojoCallLeg PCL : C.getAllCallLegs())
				{
					log.debug("###########################");
					log.debug(PCL.toString());
					if(PCL.getCallLegState().equals(CallLegState.LINKED))
					{
						if(PCL.getLineId() == 0)
						{
							TotalIntern=TotalIntern+1;
						}
						else
						{
							TotalExtern = TotalExtern+1;
							String LineName="Unbekannt";
							if(PCL.getLineName() != null)
							{
								LineName = PCL.getLineName();
							}
							if(TotalPerLine.containsKey(LineName))
							{
								TotalPerLine.put(LineName, TotalPerLine.get(LineName)+1);
							}
							else
							{
								TotalPerLine.put(LineName, 1);
							}
						}
					}
				}
		}
		
		Total = TotalIntern+TotalExtern;
	}//END OF EXECUTION



	
	
}
