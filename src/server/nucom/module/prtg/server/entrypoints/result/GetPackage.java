package nucom.module.prtg.server.entrypoints.result;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.prtg.server.utility.Storage;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Gets the current Package")
public class GetPackage implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	

	@OutputVar(label="Package", description="Current Result Package",type=VariableType.OBJECT)
	public Object Package = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		//This makes it possible, to pull the XML-Constructor from any module, and then adding their own channels to it.
		Package = Storage.XMLC;
		if(Package == null)
		{
			context.getLog().debug("PRTG Monitor did not Return a Package, is the Core Service running?");
		}
	}//END OF EXECUTION

	
}
