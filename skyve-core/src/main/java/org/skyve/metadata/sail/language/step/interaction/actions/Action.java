package org.skyve.metadata.sail.language.step.interaction.actions;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.skyve.metadata.sail.execution.Executor;
import org.skyve.metadata.sail.language.Step;
import org.skyve.impl.sail.execution.AutomationContext;
import org.skyve.impl.util.UtilImpl;
import org.skyve.impl.util.XMLMetaData;

/**
 * All explicit actions - BizImport, BizExport, Upload, Download, Report, Server etc
 * @author mike
 */
@XmlType(namespace = XMLMetaData.SAIL_NAMESPACE)
@XmlRootElement(namespace = XMLMetaData.SAIL_NAMESPACE)
public class Action implements Step {
	private String actionName;
	// Need to press OK/Yes/Continue on the confirmation dialog - true or false/null
	private Boolean confirm;
	
	public String getActionName() {
		return actionName;
	}

	@XmlAttribute(name = "name", required = true)
	public void setActionName(String actionName) {
		this.actionName = UtilImpl.processStringValue(actionName);
	}

	public Boolean getConfirm() {
		return confirm;
	}

	@XmlAttribute(name = "confirm")
	public void setConfirm(Boolean confirm) {
		this.confirm = confirm;
	}

	@Override
	public void execute(Executor executor) {
		executor.executeAction(this);
	}
	
	@Override
	public String getIdentifier(AutomationContext context) {
		return actionName;
	}
}
