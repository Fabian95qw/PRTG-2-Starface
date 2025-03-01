package si.module.prtg.sensors.iqueue;

import de.starface.core.component.StarfaceComponentProvider;
import de.starface.integration.uci.java.v30.types.QueueStatistics;
import de.starface.integration.uci.ucp.adapters.v30.UcpQueueAdapter;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility = Visibility.Private, rookieFunction = false, description = "Returns Stats on a Single IQueue")
public class IQueueStats implements IBaseExecutable
{
	// ##########################################################################################

	@InputVar(label = "STARFACE_ACCOUNT", description = "", type = VariableType.STARFACE_ACCOUNT)
	public Integer STARFACE_ACCOUNT = 0;

	@OutputVar(label = "CallersinQueue", description = "", type = VariableType.NUMBER)
	public Integer CallersinQueue = 0;

	@OutputVar(label = "MissedCalls", description = "", type = VariableType.NUMBER)
	public Integer MissedCalls = 0;

	@OutputVar(label = "UnansweredCalls", description = "", type = VariableType.NUMBER)
	public Integer UnansweredCalls = 0;

	@OutputVar(label = "TotalCalls", description = "", type = VariableType.NUMBER)
	public Integer TotalCalls = 0;

	@OutputVar(label = "AverageWaitingTime", description = "", type = VariableType.NUMBER)
	public Integer AverageWaitingTime = 0;

	@OutputVar(label = "FreeAgents", description = "", type = VariableType.NUMBER)
	public Integer FreeAgents = 0;

	StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance();
	// ##########################################################################################

	// ################### Code Execution ############################
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		UcpQueueAdapter UQA = context.provider().fetch(UcpQueueAdapter.class);

		QueueStatistics QS = UQA.getQueueStatistics(STARFACE_ACCOUNT);
		if (QS != null)
		{
			CallersinQueue = QS.getCallersInQueue();
			MissedCalls = QS.getMissedCalls();
			UnansweredCalls = QS.getUnansweredCalls();
			TotalCalls = QS.getTotalCalls();
			AverageWaitingTime = QS.getAverageWaitingTime();
			FreeAgents = QS.getFreeAgents();

		}

	}// END OF EXECUTION

}