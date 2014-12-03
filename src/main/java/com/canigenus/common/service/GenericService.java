/**
 * 
 */
package com.canigenus.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.util.CriteriaPopulator;

/**
 * @author ranjarah
 * 
 */
public interface GenericService extends Serializable {

	public <T> void persist(T model);

	public <T> T getModel(Object id, Class<T> classType);

	public <T> void delete(T model);
	public <T> void delete(Object id,  Class<T> classType);
	public <T> void deleteDetached(T model);

	<T> void persist(T model, boolean clear);

	<T> void refresh(T model);

	/**
	 * @param clazz
	 * @param id
	 * @param fieldsToLoad
	 * @return
	 */
	<T> T getPartialModel(Class<T> clazz, Object id, String... fieldsToLoad);

	/**
	 * @param clazz
	 * @param filterFieldName
	 * @param filterFieldValue
	 * @param fieldsToLoad
	 * @return
	 */
	<T> List<T> getPartialModelList(Class<T> clazz, String filterFieldName,
			Object filterFieldValue, String... fieldsToLoad);

	/**
	 * @param clazz
	 * @param filterFieldName
	 * @param filterFieldValue
	 * @return
	 */
	<T> List<T> getModelList(Class<T> clazz, String filterFieldName,
			Object filterFieldValue);

	/**
	 * @param clazz
	 * @param criteriaPopulator
	 * @param firstResult
	 * @param maxResult
	 * @param orderBy
	 * @param joinTableWithFieldsToLoad
	 * @param fieldsToLoad
	 * @return
	 */
	<T> List<T> getPartialModelListWithJoin(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy,
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad);

	/**
	 * @param clazz
	 * @param criteriaPopulator
	 * @param firstResult
	 * @param maxResult
	 * @param fieldsToLoad
	 * @return
	 */
	<T> List<T> getPartialModelList(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, String... fieldsToLoad);

	/**
	 * @param clazz
	 * @param criteriaPopulator
	 * @param firstResult
	 * @param maxResult
	 * @param orderBy
	 * @param fieldsToLoad
	 * @return
	 */
	<T> List<T> getPartialModelList(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, String... fieldsToLoad);

	/**
	 * @param clazz
	 * @param criteriaPopulator
	 * @param fieldsToLoad
	 * @return
	 */
	<T> List<T> getPartialModelList(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, String... fieldsToLoad);

	/**
	 * @param clazz
	 * @param criteriaPopulator
	 * @return
	 */
	<T> Long getModelCount(Class<T> clazz, CriteriaPopulator criteriaPopulator);

	<T> T getModelWithDepth( Object id,Class<T> type, String... fetchRelations);

	<T,U extends Number> U getSumByColumn(Class<T> clazz,Class<U> returningClass, CriteriaPopulator criteriaPopulator,
			String column);

	<T,U extends Number> U getSumByColumn(Class<T> clazz,Class<U> returningClass, CriteriaPopulator criteriaPopulator,
			String column, int start, int end);

	<T> T merge(T model);

	<T> boolean isUnique(Class<T> entity, String propertyName, Object propertyValue);

	<T> boolean isUniqueExceptThis(Class<T> entity, Identifiable<?> object,
			String propertyName, Object propertyValue);

	<T> boolean isUniqueForSaveAndUpdate(Class<T> entity, Identifiable<?> object,
			String propertyName, Object propertyValue);

	<T> Long getMaxByColumn(Class<T> clazz, String column);

    <T>	List<T> getPartialModelListWithJoin(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, boolean distinct,
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad);

	<T> T getModel(Class<T> clazz, String filterFieldName, Object filterFieldValue);

}
