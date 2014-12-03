package com.canigenus.common.bean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.canigenus.common.model.Convertible;
import com.canigenus.common.service.GenericService;

public abstract class AbstractIdConverter implements Converter{

	public abstract GenericService getService();

	public String getName() {
		return "name";
	}

	public abstract Class<? extends Convertible<?>> getClassType();

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return getService().getModelList(getClassType(), getName(), value)
				.get(0).getId();
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((Convertible<?>) value).getName();
	}
}
