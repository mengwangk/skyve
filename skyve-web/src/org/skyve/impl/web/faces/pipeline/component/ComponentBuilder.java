package org.skyve.impl.web.faces.pipeline.component;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputLink;

import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;
import org.skyve.domain.Bean;
import org.skyve.domain.types.converters.Converter;
import org.skyve.domain.types.converters.Format;
import org.skyve.impl.metadata.view.container.Tab;
import org.skyve.impl.metadata.view.container.TabPane;
import org.skyve.impl.metadata.view.reference.ReferenceTarget;
import org.skyve.impl.metadata.view.reference.ReferenceTarget.ReferenceTargetType;
import org.skyve.impl.metadata.view.widget.Blurb;
import org.skyve.impl.metadata.view.widget.Button;
import org.skyve.impl.metadata.view.widget.Link;
import org.skyve.impl.metadata.view.widget.bound.Label;
import org.skyve.impl.metadata.view.widget.bound.input.CheckBox;
import org.skyve.impl.metadata.view.widget.bound.input.ColourPicker;
import org.skyve.impl.metadata.view.widget.bound.input.Combo;
import org.skyve.impl.metadata.view.widget.bound.input.ContentImage;
import org.skyve.impl.metadata.view.widget.bound.input.ContentLink;
import org.skyve.impl.metadata.view.widget.bound.input.HTML;
import org.skyve.impl.metadata.view.widget.bound.input.ListMembership;
import org.skyve.impl.metadata.view.widget.bound.input.LookupDescription;
import org.skyve.impl.metadata.view.widget.bound.input.Password;
import org.skyve.impl.metadata.view.widget.bound.input.Radio;
import org.skyve.impl.metadata.view.widget.bound.input.RichText;
import org.skyve.impl.metadata.view.widget.bound.input.Spinner;
import org.skyve.impl.metadata.view.widget.bound.input.TextArea;
import org.skyve.impl.metadata.view.widget.bound.input.TextField;
import org.skyve.impl.metadata.view.widget.bound.tabular.DataGrid;
import org.skyve.impl.metadata.view.widget.bound.tabular.DataGridColumn;
import org.skyve.impl.web.faces.pipeline.AbstractFacesBuilder;
import org.skyve.metadata.controller.ImplicitActionName;
import org.skyve.metadata.module.query.DocumentQueryDefinition;
import org.skyve.metadata.module.query.QueryDefinition;
import org.skyve.metadata.view.Action;

public abstract class ComponentBuilder extends AbstractFacesBuilder {
	/**
	 * Used to create a visible/invisible panel for a view based to switch between create and edit views.
	 * @param invisibleConditionName
	 * @return
	 */
	public abstract UIComponent view(String invisibleConditionName);

	/**
	 * 
	 * @return	The toolbar component, or null if there is no toolbar component required for this renderer.
	 */
	public abstract UIComponent toolbar();
	
	/**
	 * 
	 * @param invisible
	 * @return
	 */
	public abstract UIComponent tabPane(TabPane tabPane);
	
	public abstract UIComponent tab(Tab tab);
	
	public abstract UIComponent border(String title,
										String invisibileConditionName,
										Integer pixelWidth);
	public abstract UIComponent label(String value);
	
	public abstract UIComponent spacer(Integer pixelWidth, Integer pixelHeight);
	
	public abstract UIComponent actionButton(String listBinding, Button button, Action action);
	public abstract UIComponent reportButton(Button button, Action action);
	
	public abstract UIComponent image(Integer pixelWidth, 
										Integer responsiveWidth,
										Integer percentageWidth, 
										Integer pixelHeight,
										Integer percentageHeight, 
										String url, 
										String invisible,
										boolean border);
	
	public abstract UIComponent blurb(String value, String binding, Blurb blurb);
	public abstract UIComponent label(String value, String binding, Label label);

	public abstract UIComponent dataGrid(DataGrid grid);
	public abstract UIComponent addDataGridBoundColumn(UIComponent current, 
														DataGrid grid,
														DataGridColumn column,
														String columnTitle,
														String columnBinding,
														StringBuilder gridColumnExpression);
	public abstract UIComponent addedDataGridBoundColumn(UIComponent current);
	public abstract UIComponent addDataGridContainerColumn(UIComponent current,
															DataGrid grid,
															DataGridColumn column);
	public abstract UIComponent addedDataGridContainerColumn(UIComponent current);
	public abstract UIComponent addDataGridActionColumn(UIComponent current, 
															DataGrid grid,
															String gridColumnExpression,
															String singluarDocumentAlias,
															boolean inline);
	
	public abstract UIComponent listGrid(DocumentQueryDefinition query, 
											boolean canCreate,
											boolean showPaginator,
											boolean stickyHeader);
	
	public abstract UIComponent listMembership(ListMembership membership);
	
	public abstract UIComponent checkBox(String listBinding, CheckBox checkBox, String title, boolean required);

	public abstract UIComponent colourPicker(String listBinding, ColourPicker colour, String title, boolean required);
	
	public abstract UIComponent combo(String listBinding, Combo combo, String title, boolean required);

	public abstract UIComponent contentImage(String listBinding, ContentImage image, String title, boolean required);

	public abstract UIComponent contentLink(String listBinding, ContentLink link, String title, boolean required);
	
	public abstract UIComponent html(String listBinding, HTML html, String title, boolean required);

	public abstract UIComponent lookupDescription(String listBinding,
													LookupDescription lookup,
													String title,
													boolean required,
													String displayBinding,
													QueryDefinition query);
	
	public abstract UIComponent password(String listBinding, Password password, String title, boolean required);

	public abstract UIComponent radio(String listBinding, Radio radio, String title, boolean required);
	
	public abstract UIComponent richText(String listBinding, RichText text, String title, boolean required);
	
	public abstract UIComponent spinner(String listBinding, Spinner spinner, String title, boolean required);
	
	public abstract UIComponent textArea(String listBinding, TextArea text, String title, boolean required, Integer length);
	
	public abstract UIComponent text(String listBinding, 
										TextField text, 
										String title, 
										boolean required,
										Integer length,
										Converter<?> converter,
										Format<?> format,
										javax.faces.convert.Converter facesConverter);
	
	public HtmlOutputLink outputLink(String listBinding, 
										String value, 
										String href, 
										String invisible,
										ReferenceTarget target) {
		HtmlOutputLink result = (HtmlOutputLink) a.createComponent(HtmlOutputLink.COMPONENT_TYPE);
		if (listBinding != null) {
			result.setValueExpression("value", createValueExpressionFromBinding(listBinding, href, true, null, String.class));
		}
		else {
			result.setValueExpression("value", createValueExpressionFromBinding(href, true, null, String.class));
		}
		if (value != null) {
			UIOutput outputText = (UIOutput) a.createComponent(UIOutput.COMPONENT_TYPE);
			outputText.setValue(value);
			result.getChildren().add(outputText);
		}
		setInvisible(result, invisible, null);

		if (target != null) {
			// modal windows are not supported
			ReferenceTargetType type = target.getType();
			if (ReferenceTargetType.blankFrame.equals(type)) {
				result.setTarget("_blank");
			}
			else if (ReferenceTargetType.namedFame.equals(type)) {
				result.setTarget(target.getName());
			}
		}

		return result;
	}
	
	public AjaxBehavior ajax(String listBinding, String actionName) {
		AjaxBehavior result = (AjaxBehavior) a.createBehavior(AjaxBehavior.BEHAVIOR_ID);
		result.setProcess(process);
		result.setUpdate(update);
		if (actionName != null) {
			MethodExpression me = methodExpressionForAction(null, actionName, listBinding, false);
			result.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(me, me));
		}

		return result;
	}
	
	public abstract UIComponent actionLink(String listBinding, Link link, String actionName);

	public abstract UIComponent report(Action action);
	
	public abstract UIComponent action(String listBinding, 
										Action action, 
										ImplicitActionName name, 
										String title);
	
	protected MethodExpression methodExpressionForAction(ImplicitActionName implicitActionName, 
															String actionName,
															String collectionName, 
															boolean inline) {
		StringBuilder expression = new StringBuilder(64);
		expression.append("#{").append(managedBeanName).append('.');
		Class<?>[] parameterTypes = null;
		if (implicitActionName != null) {
			expression.append(implicitActionName.toString().toLowerCase());
			if (collectionName != null) {
				if (ImplicitActionName.Add.equals(implicitActionName)) {
					parameterTypes = new Class[] {String.class, Boolean.class};
					expression.append("('").append(collectionName).append("',").append(inline).append(")");
				} 
				else if (ImplicitActionName.Remove.equals(implicitActionName)) {
					parameterTypes = new Class[] {String.class, String.class};
					expression.append("('").append(collectionName).append("',");
					expression.append(collectionName).append("['").append(Bean.DOCUMENT_ID).append("'])");
				} 
				else {
					parameterTypes = new Class[] {String.class, String.class};
					expression.append("('").append(collectionName).append("', ");
					expression.append(collectionName).append("['").append(Bean.DOCUMENT_ID).append("'])");
				}
			} 
			else {
				if (ImplicitActionName.Remove.equals(implicitActionName)) {
					parameterTypes = new Class[] {String.class, String.class};
					expression.append("(null,null)");
				} 
				else {
					parameterTypes = new Class[0];
				}
			}
		}
		else {
			parameterTypes = new Class[] {String.class, String.class, String.class};
			expression.append("action('").append(actionName).append('\'');
			if (collectionName != null) {
				expression.append(", '").append(collectionName).append("', ");
				expression.append(collectionName).append("['").append(Bean.DOCUMENT_ID).append("'])");
			} 
			else {
				parameterTypes = new Class[] { String.class };
				expression.append(", null, null)");
			}
		}
		expression.append('}');

		return ef.createMethodExpression(elc, expression.toString(), null, parameterTypes);
	}
}