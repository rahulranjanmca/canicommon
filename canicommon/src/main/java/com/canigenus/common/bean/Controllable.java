package com.canigenus.common.bean;

import com.canigenus.common.service.GenericService;

public interface Controllable<T> {
	public abstract GenericService getService();

	public abstract Class<T> getClassType();

	public abstract T instantiateEntity();
}
