package com.canigenus.common.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.service.GenericService;
import com.canigenus.common.util.JavaUtil;


public abstract class AbstractUniqueValidator implements Validator {
	


  public abstract GenericService getService();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (entity == null && JavaUtil.isBlank(property)) {
            return;
        }

        if (entity == null) {
            throw new IllegalStateException("Missing 'entity' attribute");
        }

        if (property == null) {
            throw new IllegalStateException("Missing 'property' attribute");
        }
        
        if (entity.getId()!=null) {
        	 if (!getService().isUniqueExceptThis(entity.getClass(), entity, property, value)) {
                 FacesMessage fm = new FacesMessage(message);
                 fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                 throw new ValidatorException(fm);
             }
        } else {
        	 if (!getService().isUnique(entity.getClass(), property, value)) {
                 FacesMessage fm = new FacesMessage(message);
                 fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                 throw new ValidatorException(fm);
             }
        }

        if (!getService().isUnique(entity.getClass(), property, value)) {
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(fm);
        }
    }

    private Identifiable<?> entity;
    private String property;

    public void setEntity(Identifiable<?> entity) {
        this.entity = entity;
    }

    public Identifiable<?> getEntity() {
        return entity;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
    
	String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
