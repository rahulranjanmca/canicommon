package com.canigenus.common.model;

import java.util.List;
import java.util.Set;

public interface IUser {

	public String getUserId();

	public void setUserId(String userId);

	public String getStatus();

	public void setStatus(String status);

	public <U extends IPassword> List<U> getPasswords();

	public <U extends Enum<?>> Set<U> getRoles();

}
