package com.canigenus.common.service;

import java.util.List;

import com.canigenus.common.model.IUser;

public abstract class AbstractUserService extends JpaGenericServiceImpl {

	private static final long serialVersionUID = 5866700361637002188L;

	public <E extends IUser> E getUserWithPasswordAndRole(Class<E> clazz,
			String userId) {
		List<E> list = getModelList(clazz, "userId", userId);
		if (list.isEmpty()) {
			return null;
		} else {
			list.get(0).getPasswords().size();
			list.get(0).getRoles().size();
			return list.get(0);
		}
	}

	public <E> boolean isUserIdExists(Class<E> clazz, String userId) {
		List<E> userDTOs = getModelList(clazz, "userId", userId);
		if (userDTOs != null && !userDTOs.isEmpty()) {
			return true;
		} else
			return false;
	}

}
