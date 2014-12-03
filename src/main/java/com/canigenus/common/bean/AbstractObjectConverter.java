package com.canigenus.common.bean;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.canigenus.common.model.Convertible;
import com.canigenus.common.service.GenericService;

public abstract class AbstractObjectConverter implements Converter {

	public abstract GenericService getService();

	public String getName() {
		return "name";
	}

	public abstract Class<? extends Convertible<?>> getClassType();

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		List<? extends Convertible<?>> list = getService().getModelList(
				getClassType(), getName(), value);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((Convertible<?>) value).getName();
	}

}