package si.module.prtg.sensors.licenses;

import de.starface.bo.UserBusinessObject;
import de.starface.bo.license.UserLicenseType;
import de.starface.license.manager.LicenseComponent;
import de.starface.license.manager.LicenseComponent.Features;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.PersonAndAccountHandler;
import de.vertico.starface.persistence.databean.core.ExtendedUserData;
import de.vertico.starface.persistence.databean.core.Permission;

@Function(visibility=Visibility.Private, description="Returns the amount of currently unused licenses")
public class UnusedUserLicenses implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Unused Licenses", description="",type=VariableType.NUMBER)
	public  Integer UnusedLicenses=0;
	
	@OutputVar(label="Unused UCC Licenses", description="",type=VariableType.NUMBER)
	public  Integer UnusedUCCLicenses=0;
	
	@OutputVar(label="Unused UCC Light Licenses", description="",type=VariableType.NUMBER)
	public  Integer UnusedLightLicenses=0;
	
	@OutputVar(label="Unused TS Licenses", description="",type=VariableType.NUMBER)
	public  Integer UnusedTSLicenses=0;
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		
		LicenseComponent LC = (LicenseComponent)context.springApplicationContext().getBean(LicenseComponent.class);		
		PersonAndAccountHandler PAH = (PersonAndAccountHandler)context.springApplicationContext().getBean(PersonAndAccountHandler.class);
		Integer AvailableUsers = PAH.getAvailableLicensesCount(UserLicenseType.USER);
		Integer AvailableUsersLight = PAH.getAvailableLicensesCount(UserLicenseType.USERLIGHT);
		Integer AvailableUCC = PAH.countSettedPermission(Permission.UCI_AUTOPROVISIONING);
		
		UnusedLicenses = AvailableUsers;
		UnusedUCCLicenses  = AvailableUCC;
		UnusedLightLicenses = AvailableUsersLight;

		UserBusinessObject UBO = (UserBusinessObject)context.springApplicationContext().getBean(UserBusinessObject.class);	
		
		Integer UsedTerminalserverLicenses=0; //Counter for Users with TS Lcence
		for(ExtendedUserData EUD : UBO.getUsers()) //Get all Users
		{
			if(EUD.getPermissions().contains(Permission.WINCLIENT_TERMINAL_SERVER))//If User has Permission for TS Server 
			{
				UsedTerminalserverLicenses=UsedTerminalserverLicenses+1; //Add 1 to Counter
			}
		}
		
		Integer TotalTerminalserverLicenses = LC.getLicensedUsersOfFeature(Features.WINCLIENT_TERMINAL_SERVER); //Get Total amount of TS Licenses based on the Feature
		UnusedTSLicenses = TotalTerminalserverLicenses-UsedTerminalserverLicenses; // Calculate the Total of Unused licenses
	
	}//END OF EXECUTION

	
}
