package com.canigenus.common.model;

public interface IPassword extends Identifiable<Long>{
	public String getPassword();

	public String getSalt();

	public void setPassword(String password);

	public void setSalt(String salt);

	public String getConfirmPassword();

	public void setOneTimeKey(String key);

	public String getOneTimeKey();

	public void setOldPassword(String oldPassword);

	public String getOldPassword();
}
