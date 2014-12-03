/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.canigenus.common.bean;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.service.GenericService;
import com.canigenus.common.util.CriteriaPopulator;
import com.canigenus.common.util.JPADataModelForPrimeFaces;
import com.canigenus.common.util.JsfUtil;

/**
 * 
 * @author Rahul
 */
public abstract class GenericJsfController<T extends Identifiable<?>> implements
		Serializable, Controllable<T> {

	protected void addMessage(String str) {
		FacesMessage msg = new FacesMessage(str);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	protected void removeBeanFromSession(String beanName) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.removeAttribute(beanName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8983428198453421888L;

	private T current;
	private JPADataModelForPrimeFaces<T> items = null;

	public GenericJsfController() {

	}

	public abstract GenericService getService();

	public abstract Class<T> getClassType();

	public abstract T instantiateEntity();

	public T getSelected() {
		if (current == null) {
			current = instantiateEntity();
		}
		return current;
	}

	
	 public void search() {
			items = new JPADataModelForPrimeFaces<T>(getService(),getCriteriaPopulator(), getClassType(), null);
		}
		
	public String prepareList() {
		recreateModel();
		return "List";
	}

	public String prepareView() {
		return "View";
	}

	public String prepareCreate() {
		current = instantiateEntity();
		return "Edit";
	}

	public String create() {
		try {
			getService().merge(current);
			JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle")
					.getString(getClassType().getSimpleName()+"Created"));
			return "List";
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle")
					.getString("PersistenceErrorOccured"));
			return null;
		}
	}

	public String prepareEdit() {
		return "Edit";
	}

	public String update() {
		try {
			getService().merge(current);
			JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle")
					.getString(getClassType().getSimpleName()+"Updated"));
			return "List";
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle")
					.getString("PersistenceErrorOccured"));
			return null;
		}
	}


	private void recreateModel() {
		items = null;
	}

	public String destroy() {

		try {
			getService().deleteDetached(current);
			JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle")
					.getString(getClassType().getSimpleName()+"Deleted"));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle")
					.getString("PersistenceErrorOccured"));
			return null;
		}

		search();

		return "List";

	}

	public SelectItem[] getItemsAvailableSelectMany() {
		return JsfUtil.getSelectItems(
				getService().getPartialModelList(getClassType(), null), false);
	}

	public SelectItem[] getItemsAvailableSelectOne() {
		return JsfUtil.getSelectItems(
				getService().getPartialModelList(getClassType(), null), true);
	}

	public T getCurrent() {
		return current;
	}

	public void setCurrent(T current) {
		this.current = current;
	}

	public JPADataModelForPrimeFaces<T> getItems() {
		return items;
	}

	public void setItems(JPADataModelForPrimeFaces<T> items) {
		this.items = items;
	}

	public abstract CriteriaPopulator<?> getCriteriaPopulator();

}
