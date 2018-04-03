package nucom.module.prtg.server.entrypoints.result;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import nucom.module.prtg.server.utility.Storage;
import nucom.module.prtg.server.xml.XMLConstructor;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Commits this Package. The Package is now ready for pickup by the prtg server")
public class CommitPackage implements IBaseExecutable 
{
	//###################			Starface GUI Variables		############################	

	@InputVar(label="Package", description="Package to Commit" ,type=VariableType.OBJECT)
	public Object Package = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		//NOTE: Override the currently Stored Package with a new one
		Storage.XMLC = (XMLConstructor) Package;
	}//END OF EXECUTION

	
}
