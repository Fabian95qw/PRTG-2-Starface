package nucom.module.prtg.sensors.lines;

import java.util.List;

import de.starface.ch.processing.callactions.CallActions;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns the Amount of Lines currently Online, and Offline")
public class LinesUpDown implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Lines Up", description="",type=VariableType.NUMBER)
	public  Integer LinesUp = 0;
	
	@OutputVar(label="Lines Down", description="",type=VariableType.NUMBER)
	public  Integer LinesDown = 0;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		CallActions CA = (CallActions)context.provider().fetch(CallActions.class);

		List<String> Lines = CA.sipShowRegistry();
		
		Lines.remove(0);
		Lines.remove(Lines.size()-1);
		
		for (String Line : Lines)
		{
			context.getLog().debug(Line);
			if(Line.contains("Registered"))
			{
				LinesUp++;
			}
			else
			{
				LinesDown++;
			}
		}
		
				
	}//END OF EXECUTION

	
}
