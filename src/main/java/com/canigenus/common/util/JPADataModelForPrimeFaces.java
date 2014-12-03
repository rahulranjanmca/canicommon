package com.canigenus.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.service.GenericService;

public class JPADataModelForPrimeFaces<T extends Identifiable<?>> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	protected GenericService service;

	protected Class<T> entityClass;

	protected String[] fieldsToLoad;

	protected CriteriaPopulator<?> criteriaPopulator;
	
	protected Map<String, Boolean> orderBy;

	protected Map<String, T> result = new HashMap<String, T>();

	
	
	/*//@Override
	public <E> List<T> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {
		List<T> list= service.getPartialModelList(entityClass, criteriaPopulator,
				first, pageSize, orderBy, fieldsToLoad);
		for(T t:list)
		{
			result.put(t.getId().toString(), t);
		}
		return list;
	}
	*/
	
	@Override
	public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters) {
		List<T> list= service.getPartialModelList(entityClass, criteriaPopulator,
				first, pageSize, orderBy, fieldsToLoad);
		for(T t:list)
		{
			result.put(t.getId().toString(), t);
		}
		return list;
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<T> list= service.getPartialModelList(entityClass, criteriaPopulator,
				first, pageSize, orderBy, fieldsToLoad);
		for(T t:list)
		{
			result.put(t.getId().toString(), t);
		}
		return list;
	}

	/*//@Override
	public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta,
			Map<String, String> filters) {

		List<T> list= service.getPartialModelList(entityClass, criteriaPopulator,
				first, pageSize, orderBy, fieldsToLoad);
		for(T t:list)
		{
			result.put(t.getId().toString(), t);
		}
		return list;
	}
	*/
	public JPADataModelForPrimeFaces(GenericService service,
			CriteriaPopulator<?> populator, Class<T> entityClass, Map<String, Boolean> orderBy) {
		super();
		this.service = service;
		this.entityClass = entityClass;
		this.criteriaPopulator=populator;
		this.setRowCount(service.getModelCount(entityClass, populator)
				.intValue());
	}

	protected Class<T> getEntityClass() {
		return entityClass;
	}

	protected Expression<Boolean> createFilterCriteriaForField(
			String propertyName, Object filterValue, Root<T> root,
			CriteriaBuilder criteriaBuilder) {
		String stringFilterValue = (String) filterValue;
		if (stringFilterValue==null || stringFilterValue.isEmpty()) {
			return null;
		}
		Path<String> expression = root.get(propertyName);
		Expression<Integer> locator = criteriaBuilder.locate(
				criteriaBuilder.lower(expression), stringFilterValue);
		return criteriaBuilder.gt(locator, 0);
	}

	@Override
	public T getRowData(String rowKey) {
		return result.get(rowKey);
	}

	@Override
	public void setRowIndex(int rowIndex)
	{
	    if(rowIndex==-1||getPageSize()==0)
	    {
	        super.setRowIndex(-1);
	    }
	    else
	    {
	         super.setRowIndex(rowIndex%getPageSize());
	    }
	}

	public Map<String, T> getResult() {
		return result;
	}

	public String[] getFieldsToLoad() {
		return fieldsToLoad;
	}

	public void setFieldsToLoad(String[] fieldsToLoad) {
		this.fieldsToLoad = fieldsToLoad;
	}
	
	

}
