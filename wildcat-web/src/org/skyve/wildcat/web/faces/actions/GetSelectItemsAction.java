package org.skyve.wildcat.web.faces.actions;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.skyve.CORE;
import org.skyve.domain.Bean;
import org.skyve.metadata.customer.Customer;
import org.skyve.metadata.model.Attribute;
import org.skyve.metadata.model.document.Bizlet.DomainValue;
import org.skyve.metadata.model.document.Document;
import org.skyve.metadata.model.document.DomainType;
import org.skyve.metadata.module.Module;
import org.skyve.util.Binder;
import org.skyve.util.Binder.TargetMetaData;
import org.skyve.wildcat.metadata.model.document.Association;
import org.skyve.wildcat.metadata.model.document.DocumentImpl;
import org.skyve.wildcat.metadata.repository.AbstractRepository;
import org.skyve.wildcat.web.faces.FacesAction;

public class GetSelectItemsAction extends FacesAction<List<SelectItem>> {
	private Bean bean;
	private String binding;
	private boolean includeEmptyItem;

	public GetSelectItemsAction(Bean bean, String binding, boolean includeEmptyItem) {
		this.bean = bean;
		this.binding = binding;
		this.includeEmptyItem = includeEmptyItem;
	}

	@Override
	public List<SelectItem> callback() throws Exception {
    	String bizModule = bean.getBizModule();
    	String bizDocument = bean.getBizDocument();

    	Customer customer = CORE.getUser().getCustomer();
        Module module = customer.getModule(bizModule);
        Document document = module.getDocument(customer, bizDocument);
        TargetMetaData target = Binder.getMetaDataForBinding(customer, module, document, binding);
        Attribute targetAttribute = target.getAttribute();
        Document targetDocument = target.getDocument();

        List<SelectItem> result = null;
        
        if ((targetDocument != null) && (targetAttribute != null)) {
            DomainType domainType = targetAttribute.getDomainType();
            Bean owningBean = bean;
            int lastDotIndex = binding.lastIndexOf('.');
            if (lastDotIndex > 0) {
            	owningBean = (Bean) Binder.get(bean, binding.substring(0, lastDotIndex));
            }

            List<DomainValue> domainValues = ((DocumentImpl) targetDocument).getDomainValues(
																					            				customer,
																					                            domainType,
																					                            targetAttribute,
																					                            owningBean);
            if (includeEmptyItem) {
	            result = new ArrayList<>(domainValues.size() + 1);
	        	// add an empty select item so that a null value 
	        	// in the bean can be represented, even if mandatory
	        	if (targetAttribute.isRequired()) {
	        		result.add(new SelectItem(null, "", "", true, false, true)); // mandatory gets an unselectable item
	        	}
	        	else {
	        		result.add(new SelectItem(null, "")); // optional gets a selectable item
	        	}
            }
            else {
	            result = new ArrayList<>(domainValues.size());
            }

            Class<?> type = null;
        	for (DomainValue domainValue : domainValues) {
            	String code = domainValue.getCode();
            	Object value = code;
            	if (targetAttribute instanceof org.skyve.wildcat.metadata.model.document.field.Enumeration) {
            		if (type == null) {
            			type = AbstractRepository.get().getEnum((org.skyve.wildcat.metadata.model.document.field.Enumeration) targetAttribute); 
            		}
        			value = Binder.convert(type, code);
            	}
            	else if (targetAttribute instanceof Association) {
            		Association targetAssociation = (Association) targetAttribute;
            		value = CORE.getPersistence().retrieve(targetDocument.getOwningModuleName(), 
                											targetAssociation.getDocumentName(),
                											code,
                											false);
            	}
            	result.add(new SelectItem(value, domainValue.getDescription()));
            }
        }
        else {
        	result = new ArrayList<>(0);
        }

        return result;
	}
}
