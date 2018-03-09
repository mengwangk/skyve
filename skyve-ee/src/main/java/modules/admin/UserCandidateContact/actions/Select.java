package modules.admin.UserCandidateContact.actions;

import modules.admin.User.actions.GenerateUniqueUserName;
import modules.admin.domain.User;
import modules.admin.domain.UserCandidateContact;
import modules.admin.domain.User.WizardState;

import org.skyve.metadata.controller.ServerSideAction;
import org.skyve.metadata.controller.ServerSideActionResult;
import org.skyve.web.WebContext;

public class Select implements ServerSideAction<UserCandidateContact> {
	/**
	 * For Serialization.
	 */
	private static final long serialVersionUID = 4813596591072958231L;

	@Override
	public ServerSideActionResult<UserCandidateContact> execute(UserCandidateContact candidate, WebContext webContext)
	throws Exception {
		User user = candidate.getParent();
		user.setContact(candidate.getContact());
		user.getCandidateContacts().clear();
		
		user.setWizardState(WizardState.confirmUserNameAndPassword);
		user.setUserName(GenerateUniqueUserName.generateUniqueUserNameFromContactName(user));
		
		return new ServerSideActionResult<>(candidate);
	}
}