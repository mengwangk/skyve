package modules.admin.DataMaintenance.actions;

import org.skyve.CORE;
import org.skyve.metadata.controller.ServerSideAction;
import org.skyve.metadata.controller.ServerSideActionResult;
import org.skyve.persistence.Persistence;
import org.skyve.web.WebContext;

import modules.admin.domain.DataMaintenance;

public class CheckAuditMatches implements ServerSideAction<DataMaintenance> {
	private static final long serialVersionUID = -8003482363810304078L;

	@Override
	public ServerSideActionResult execute(DataMaintenance bean, WebContext webContext)
	throws Exception {
		
		Persistence pers = CORE.getPersistence();
		bean = TruncateAuditLog.setResultCount(pers, bean);
		
		return new ServerSideActionResult(bean);
	}
}
