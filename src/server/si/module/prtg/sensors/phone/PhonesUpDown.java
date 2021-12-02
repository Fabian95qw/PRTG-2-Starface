package si.module.prtg.sensors.phone;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.config.phone.forms.PhoneListBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.module.core.runtime.functions.system.Execute4;
import de.vertico.starface.persistence.connector.SipAndPhonesHandler;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class PhonesUpDown implements IBaseExecutable 
{
	//##########################################################################################		    	
	@OutputVar(label="PhonesUp", description="",type=VariableType.NUMBER)
	public Integer PhonesUp = 0;
	
	@OutputVar(label="PhonesDown", description="",type=VariableType.NUMBER)
	public Integer PhonesDown = 0;
		
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{		
		SipAndPhonesHandler SAP = (SipAndPhonesHandler)context.provider().fetch(SipAndPhonesHandler.class);

		Log log = context.getLog();
		
		//asterisk -rx "sip show peers" | grep "D" | grep "0        " | grep 1002.ylnkt22 | wc -l

		Execute4 E = new Execute4();
		E.executeAs = "Asterisk CLI Command";
		E.command = "sip show peers";
		
		E.execute(context);
		
		//log.debug(E.output);		
		
		String[] Lines = E.output.split(System.lineSeparator());
		
		for(PhoneListBean PCB : SAP.getPhoneList())
		{
			//log.debug(PCB.getSipname());
			for(String Line : Lines)
			{
				if(Line.contains(PCB.getSipname()) && Line.contains("D  ") && Line.contains("0        "))
				{
					log.debug(PCB.getSipname()+ " is down");
					PhonesDown++;
					continue;
				}
				else if(Line.contains(PCB.getSipname()) && Line.contains("D  ") && !Line.contains("0        "))
				{
					log.debug(PCB.getSipname()+ " is up");
					PhonesUp++;
					continue;
				}
			}

		}
		
	}//END OF EXECUTION

	
}
