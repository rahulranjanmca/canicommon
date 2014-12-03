/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.canigenus.common.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.service.JpaGenericServiceImpl;

/**
 * 
 * @author Rahul
 */
public abstract class GenericJsfSearchController<T extends Identifiable<?>> implements
		Serializable {

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

	@ManagedProperty("#{param.page}")
	protected Integer page;
	@ManagedProperty("#{param.sort}")
	protected String sortField;
	@ManagedProperty("#{param.order}")
	protected String sortOrder;

	protected T current;
	
	public GenericJsfSearchController() {

	}

	public abstract JpaGenericServiceImpl getService();

	public abstract Class<T> getClassType();
	

	public abstract void search();
	
	

	public T getCurrent() {
		return current;
	}

	public void setCurrent(T current) {
		this.current = current;
	}

	

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
