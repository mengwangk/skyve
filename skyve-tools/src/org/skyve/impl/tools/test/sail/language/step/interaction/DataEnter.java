package org.skyve.impl.tools.test.sail.language.step.interaction;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.skyve.impl.tools.test.sail.XMLUtil;
import org.skyve.impl.tools.test.sail.language.Step;

/**
 * Set the control representing the given binding to the given value.
 * @author mike
 */
@XmlType(namespace = XMLUtil.SAIL_NAMESPACE)
@XmlRootElement(namespace = XMLUtil.SAIL_NAMESPACE)
public class DataEnter implements Step {
	private String binding;
	private String value;
	
	public String getBinding() {
		return binding;
	}

	@XmlAttribute(name = "binding", required = true)
	public void setBinding(String binding) {
		this.binding = binding;
	}

	public String getValue() {
		return value;
	}

	@XmlAttribute(name = "value", required = true)
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void execute(StringBuilder script) {
	}
}
