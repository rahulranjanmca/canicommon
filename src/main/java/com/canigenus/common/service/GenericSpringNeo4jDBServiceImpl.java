/*package com.canigenus.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.util.CriteriaPopulator;

*//**
 * 
 *//*

*//**
 * @author RanjaRah
 * 
 *//*
public abstract class GenericSpringNeo4jDBServiceImpl implements GenericService {

	private static final long serialVersionUID = 1L;

	@Override
	public <T> T getPartialModel(Class<T> clazz, Object id,
			String... fieldsToLoad) {
	  Query query= new Query();
		
		CriteriaBuilder criteriaBuilder = getNeo4jTemplate()
				.getCriteriaBuilder();
		CriteriaQuery<T> cq = criteriaBuilder.createQuery(clazz);
		Root<T> root = cq.from(clazz);
		cq.where(criteriaBuilder.equal(root.get("id"), id));
		List<Selection<?>> selections = new ArrayList<Selection<?>>();
		for (String fieldToLoad : fieldsToLoad) {
			selections.add(root.get(fieldToLoad));
		}
		cq.multiselect(selections);
		cq.orderBy(criteriaBuilder.desc(root.get("id")));
		return getNeo4jTemplate().createQuery(cq).getSingleResult();
	}

	@Override
	public <T> boolean isUnique(Class<T> entity, String propertyName,
			Object propertyValue) {
		if (entity == null || propertyValue == null) {
			return true;
		}
		return getNeo4jTemplate()
				.createQuery(
						String.format(
								"select count(c) from %s c where %s = :propertyValue",
								entity.getSimpleName(), propertyName),
						Long.class)
				.setParameter("propertyValue", propertyValue) //
				.getSingleResult() == 0;
	}

	@Override
	public <T> boolean isUniqueExceptThis(Class<T> entity,
			Identifiable<?> object, String propertyName, Object propertyValue) {
		if (entity == null || propertyValue == null) {
			return true;
		}
		return getNeo4jTemplate()
				//
				.createQuery(
						//
						String.format(
								"select count(c) from %s c where %s = :propertyValue and %s != :id", //
								entity.getSimpleName(), propertyName, "id"),
						Long.class) //
				.setParameter("propertyValue", propertyValue) //
				.setParameter("id", object.getId()) //
				.getSingleResult() == 0;
	}

	@Override
	public <T> boolean isUniqueForSaveAndUpdate(Class<T> entity,
			Identifiable<?> object, String propertyName, Object propertyValue) {
		if (entity == null || propertyValue == null) {
			return true;
		}
		if (object.getId() != null) {
			return isUniqueExceptThis(entity, object, propertyName,
					propertyValue);

		} else {
			return isUnique(entity, propertyName, propertyValue);
		}
	}

	@Override
	public <T> List<T> getPartialModelList(Class<T> clazz,
			String filterFieldName, Object filterFieldValue,
			String... fieldsToLoad) {
		CriteriaBuilder criteriaBuilder = getNeo4jTemplate()
				.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
		Root<T> r = criteriaQuery.from(clazz);
		criteriaQuery.where(criteriaBuilder.equal(r.get(filterFieldName),
				filterFieldValue));
		List<Selection<?>> selections = new ArrayList<Selection<?>>();
		for (String fieldToLoad : fieldsToLoad) {
			selections.add(r.get(fieldToLoad));
		}
		if (!selections.isEmpty()) {
			criteriaQuery.multiselect(selections);
		}
		criteriaQuery.orderBy(criteriaBuilder.desc(r.get("id")));
		return getNeo4jTemplate().createQuery(criteriaQuery).getResultList();
	}

	@Override
	public <T> Long getModelCount(Class<T> clazz,
			CriteriaPopulator criteriaPopulator) {
		CriteriaBuilder cb = getNeo4jTemplate().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(clazz)));
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (criteriaPopulator != null) {
			criteriaPopulator.populateCriteria(cb, cq, predicates);
		}
		Predicate[] array = new Predicate[predicates.size()];
		array = predicates.toArray(array);
		cq.where(cb.and(array));

		return getNeo4jTemplate().createQuery(cq).getSingleResult();
	}

	@Override
	public <T, U extends Number> U getSumByColumn(Class<T> clazz,
			Class<U> returningClass, CriteriaPopulator criteriaPopulator,
			String column) {
		return getSumByColumn(clazz, returningClass, criteriaPopulator, column,
				-1, -1);
	}

	@Override
	public <T, U extends Number> U getSumByColumn(Class<T> clazz,
			Class<U> returningClass, CriteriaPopulator criteriaPopulator,
			String column, int start, int end) {
		CriteriaBuilder cb = getNeo4jTemplate().getCriteriaBuilder();
		CriteriaQuery<U> cq = cb.createQuery(returningClass);
		Root<T> personEntity = cq.from(clazz);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (criteriaPopulator != null) {
			criteriaPopulator.populateCriteria(cb, cq, predicates);
		}

		Predicate[] array = new Predicate[predicates.size()];
		array = predicates.toArray(array);
		cq.where(cb.and(array));

		Path<U> ageAttr = personEntity.<U> get(column);
		cq = cq.select(cb.sum(ageAttr));

		TypedQuery<U> typedQuery = getNeo4jTemplate().createQuery(cq);
		if (start > 0) {
			typedQuery.setMaxResults(start);
			typedQuery.setFirstResult(end);
		}
		return typedQuery.getSingleResult();

	}

	@Override
	public <T> Long getMaxByColumn(Class<T> clazz, String column) {
		CriteriaBuilder cb = getNeo4jTemplate().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> personEntity = cq.from(clazz);
		Path<Long> ageAttr = personEntity.<Long> get(column);
		cq = cq.select(cb.max(ageAttr));
		TypedQuery<Long> typedQuery = getNeo4jTemplate().createQuery(cq);
		Long number = typedQuery.getSingleResult();
		if (number == null) {
			return new Long(0);
		} else
			return number;

	}

	@Override
	public <T> List<T> getPartialModelList(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, String... fieldsToLoad) {
		return getPartialModelList(clazz, criteriaPopulator, -1, -1,
				fieldsToLoad);
	}

	@Override
	public <T> List<T> getPartialModelList(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, String... fieldsToLoad) {
		return getPartialModelListWithJoin(clazz, criteriaPopulator,
				firstResult, maxResult, orderBy, null, fieldsToLoad);
	}

	@Override
	public <T> List<T> getPartialModelList(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, String... fieldsToLoad) {
		return getPartialModelList(clazz, criteriaPopulator, firstResult,
				maxResult, null, fieldsToLoad);
	}
	
	@Override
	public <T> List<T> getPartialModelListWithJoin(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy,
			Map<String, Set<String>> joinTableWithFieldsToLoad, String... fieldsToLoad){
		return getPartialModelListWithJoin(clazz, criteriaPopulator, firstResult, maxResult, orderBy, false, joinTableWithFieldsToLoad, fieldsToLoad);
		
	}

	@Override
	public <T> List<T> getPartialModelListWithJoin(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, boolean distinct, 
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad) {
		CriteriaBuilder cb = getNeo4jTemplate().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> r = cq.from(clazz);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (criteriaPopulator != null) {
			criteriaPopulator.populateCriteria(cb, cq, predicates);
		}
		Predicate[] array = new Predicate[predicates.size()];
		array = predicates.toArray(array);
		cq.where(cb.and(array));

		List<Selection<?>> selections = new ArrayList<Selection<?>>();
		if (fieldsToLoad != null) {
			for (String fieldToLoad : fieldsToLoad) {
				selections.add(r.get(fieldToLoad));

			}
		}
		if (joinTableWithFieldsToLoad != null) {
			for (String joinTable : joinTableWithFieldsToLoad.keySet()) {
				@SuppressWarnings("unchecked")
				Join<Object, Object> join = (Join<Object, Object>) r.fetch(
						joinTable, JoinType.LEFT);

				for (String fieldsToLoad2 : joinTableWithFieldsToLoad
						.get(joinTable)) {

					selections.add(join.get(fieldsToLoad2));
				}
			}
		}
		if (!selections.isEmpty()) {
			cq.multiselect(selections);
		}
		if (orderBy == null) {
			cq.orderBy(cb.desc(r.get("id")));
		} else {
			for (Map.Entry<String, Boolean> order : orderBy.entrySet()) {
				if (order.getValue() == false) {
					cq.orderBy(cb.desc(r.get(order.getKey())));
				} else {
					cq.orderBy(cb.asc(r.get(order.getKey())));
				}

			}
		}
		if(distinct)
		{
		cq.distinct(true); 
		}
		TypedQuery<T> typedQuery = getNeo4jTemplate().createQuery(cq);
		if (maxResult > 0) {
			typedQuery.setMaxResults(maxResult);
			typedQuery.setFirstResult(firstResult);
		}

		return typedQuery.getResultList();
	}

	@Override
	public <T> List<T> getModelList(Class<T> clazz, String filterFieldName,
			Object filterFieldValue) {
		CriteriaBuilder cb = getNeo4jTemplate().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> r = cq.from(clazz);
		if (filterFieldValue == null) {
			cq.where(cb.isNull(r.get(filterFieldName)));
		} else {
			cq.where(cb.equal(r.get(filterFieldName), filterFieldValue));
		}
		cq.orderBy(cb.desc(r.get("id")));
		return getNeo4jTemplate().createQuery(cq).getResultList();
	}
	
	@Override
	public <T> T getModel(Class<T> clazz, String filterFieldName,
			Object filterFieldValue) {
		CriteriaBuilder cb = getNeo4jTemplate().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> r = cq.from(clazz);
		if (filterFieldValue == null) {
			cq.where(cb.isNull(r.get(filterFieldName)));
		} else {
			cq.where(cb.equal(r.get(filterFieldName), filterFieldValue));
		}
		return getNeo4jTemplate().createQuery(cq).getSingleResult();
	}


	@Override
	public <T> T merge(T model) {
		return getNeo4jTemplate().merge(model);
	}

	@Override
	public <T> void persist(T model) {
		getNeo4jTemplate().persist(model);
	}

	@Override
	public <T> void refresh(T model) {
		if (!getNeo4jTemplate().contains(model)) {
			getNeo4jTemplate().merge(model);
		}
		getNeo4jTemplate().refresh(model);
	}

	@Override
	public <T> void persist(T model, boolean clear) {
		if (clear)
			getNeo4jTemplate().clear();
		getNeo4jTemplate().merge(model);
	}

	@Override
	public <T> void delete(T model) {
		getNeo4jTemplate().remove(model);
	}

	@Override
	public <T> T getModel(Object id, Class<T> classType) {
		T t = getNeo4jTemplate().find(classType, id);
		return t;
	}

	@Override
	public <T> T getModelWithDepth(Object id, Class<T> type,
			String... fetchRelations) {
		CriteriaBuilder criteriaBuilder = getNeo4jTemplate()
				.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
		Root<T> root = criteriaQuery.from(type);

		for (String relation : fetchRelations) {
			FetchParent<T, T> fetch = root;
			for (String pathSegment : relation.split("\\.")) {
				fetch = fetch.fetch(pathSegment, JoinType.LEFT);

			}
		}

		criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));

		return getNeo4jTemplate().createQuery(criteriaQuery).getSingleResult();
	}

	@Override
	public <T> void deleteDetached(T model) {
		getNeo4jTemplate().delete(model);
		

	}

	public abstract Neo4jTemplate getNeo4jTemplate();
	
	
}
*/