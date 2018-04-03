package nucom.module.prtg.server.entrypoints.result;

import java.util.Map;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import nucom.module.prtg.server.utility.EnumHelper.Unit;
import nucom.module.prtg.server.xml.XMLConstructor;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Creates an Empty Result Package")
public class AddResultToPackage implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	

	@InputVar(label="Package", description="Empty Result Package",type=VariableType.OBJECT)
	public Object Package = null;
	
	@InputVar(label="Channelname", description="Channelname used for the Result",type=VariableType.STRING)
	public String Channelname="";
	
	@InputVar(label="Value", description="Value to be set in this Channel",type=VariableType.STRING)
	public String Value="";
		
	@InputVar(label="Unit", description="Unit to use for this Channel, if custom is chosen it will display your custom unit from the field \"CustomUnit\" ", valueByReferenceAllowed=true)
	public Unit U  = Unit.Custom;
		
	@InputVar(label="CustomUnit", description="CustomUnit to Display",type=VariableType.STRING)
	public String CustomUnit="";
	
	@InputVar(label="Addition Parameters", description="Addition Parameters to Add to the Channel, in the Format <Elementname, Value>",type=VariableType.MAP)
	public Map<String, String>Params = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		if(Package != null)
		{
			XMLConstructor XMLC = (XMLConstructor) Package;
			XMLC.AddResult(Channelname, Value, U, CustomUnit, Params);
		}
		else
		{
			context.getLog().debug("Could not add Result to Package, becuase Package is null");
		}
	}//END OF EXECUTION

	
}
