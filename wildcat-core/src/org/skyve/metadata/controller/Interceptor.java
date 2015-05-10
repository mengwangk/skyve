package org.skyve.metadata.controller;

import java.util.List;

import org.skyve.bizport.BizPortException;
import org.skyve.bizport.BizPortWorkbook;
import org.skyve.domain.Bean;
import org.skyve.domain.PersistentBean;
import org.skyve.domain.messages.ValidationException;
import org.skyve.metadata.MetaData;
import org.skyve.metadata.controller.UploadAction.UploadedFile;
import org.skyve.metadata.model.document.Document;
import org.skyve.metadata.model.document.Bizlet.DomainValue;
import org.skyve.web.WebContext;

/**
 * Extend this class to enable before and after interception of events.
 * 
 * @author sandsm01
 */
public abstract class Interceptor implements MetaData {
	private static final long serialVersionUID = -5377580171203674300L;

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeNewInstance(Bean bean)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterNewInstance(Bean bean)
	throws Exception {
		// no-op
	}
	
	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeValidate(Bean bean, ValidationException e)
	throws Exception {
		return false;
	}

	@SuppressWarnings("unused")
	public void afterValidate(Bean bean, ValidationException e)
	throws Exception {
		// no-op
	}
	
	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeGetConstantDomainValues(String attributeName)
	throws Exception {
		return false;
	}

	@SuppressWarnings("unused")
	public void afterGetConstantDomainValues(String attributeName, List<DomainValue> result)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeGetVariantDomainValues(String attributeName)
	throws Exception {
		return false;
	}

	@SuppressWarnings("unused")
	public void afterGetVariantDomainValues(String attributeName, List<DomainValue> result)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeGetDynamicDomainValues(String attributeName, Bean bean)
	throws Exception {
		return false;
	}

	@SuppressWarnings("unused")
	public void afterGetDynamicDomainValues(String attributeName, Bean bean, List<DomainValue> result)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeSave(Document document, PersistentBean bean)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterSave(Document document, PersistentBean result)
	throws Exception {
		// no-op
	}
	
	@SuppressWarnings({"unused", "static-method"})
	public boolean beforePreSave(Bean bean)
	throws Exception {
		return false;
	}

	@SuppressWarnings("unused")
	public void afterPreSave(Bean bean)
	throws Exception {
		// no-op
	}
	
	@SuppressWarnings({"unused", "static-method"})
	public boolean beforePostSave(Bean bean)
	throws Exception {
		return false;
	}

	@SuppressWarnings("unused")
	public void afterPostSave(Bean bean)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeDelete(Document document, PersistentBean bean)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterDelete(Document document, PersistentBean bean)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforePreDelete(PersistentBean bean)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterPreDelete(PersistentBean bean)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforePostLoad(PersistentBean bean)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterPostLoad(PersistentBean bean)
	throws Exception {
		// no-op
	}
	
	@SuppressWarnings({"unused", "static-method"})
	public boolean beforePreExecute(ImplicitActionName actionName, Bean bean, Bean parentBean, WebContext webContext) 
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterPreExecute(ImplicitActionName actionName, Bean result, Bean parentBean, WebContext webContext)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeServerSideAction(Document document, 
											String actionName, 
											Bean bean, 
											WebContext webContext)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterServerSideAction(Document document, 
										String actionName, 
										ServerSideActionResult result, 
										WebContext webContext)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeUploadAction(Document document, 
										String actionName,
										Bean bean, 
										UploadedFile file, 
										WebContext webContext)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterUploadAction(Document document,
									String actionName,
									Bean bean, 
									UploadedFile file, 
									WebContext webContext)
	throws Exception {
		// no-op
	}
	
	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeBizImportAction(Document document, 
											String actionName,
											BizPortWorkbook bizPortable, 
											BizPortException problems)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterBizImportAction(Document document,
										String actionName,
										BizPortWorkbook bizPortable,
										BizPortException problems)
	throws Exception {
		// no-op
	}

	@SuppressWarnings({"unused", "static-method"})
	public boolean beforeBizExportAction(Document document,
											String actionName,
											WebContext webContext)
	throws Exception {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void afterBizExportAction(Document document,
										String actionName,
										BizPortWorkbook result,
										WebContext webContext)
	throws Exception {
		// no-op
	}
}