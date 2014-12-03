package com.canigenus.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.util.CriteriaPopulator;
import com.canigenus.common.util.SpringMongoDBCriteriaHelper;

/**
 * 
 */

/**
 * @author RanjaRah
 * 
 */
public abstract class GenericSpringMongoDBServiceImpl implements GenericService {

	private static final long serialVersionUID = 1L;

	@Override
	public <T> T getPartialModel(Class<T> clazz, Object id,
			String... fieldsToLoad) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and((String) id);
		query.addCriteria(criteria);
		return getMongoTeplate().findOne(query, clazz);
	}

	@Override
	public <T> boolean isUnique(Class<T> entity, String propertyName,
			Object propertyValue) {
		if (entity == null || propertyValue == null) {
			return true;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where(propertyName).is(propertyValue));
		return getMongoTeplate().count(query, entity) == 0;
	}

	@Override
	public <T> boolean isUniqueExceptThis(Class<T> entity,
			Identifiable<?> object, String propertyName, Object propertyValue) {
		Query query = new Query();
		query.addCriteria(Criteria.where(propertyName).is(propertyValue)
				.andOperator(Criteria.where("_id").is(object.getId())));
		return getMongoTeplate().count(query, entity) == 0;

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
		Query query = new Query();
		query.addCriteria(Criteria.where(filterFieldName).is(filterFieldValue));
		for (String field : fieldsToLoad) {
			query.fields().include(field);
		}
		return getMongoTeplate().find(query, clazz);

	}

	@Override
	public <T> Long getModelCount(Class<T> clazz,
			CriteriaPopulator criteriaPopulator) {
		Query query = new Query();
		if (criteriaPopulator != null) {
			SpringMongoDBCriteriaHelper<T> mongoDb = new SpringMongoDBCriteriaHelper<>();
			mongoDb.setQuery(query);
			criteriaPopulator.populateCriteria(mongoDb);
		}
		return getMongoTeplate().count(query, clazz);
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
		/*
		 * CriteriaBuilder cb = getMongoTeplate().getCriteriaBuilder();
		 * CriteriaQuery<U> cq = cb.createQuery(returningClass); Root<T>
		 * personEntity = cq.from(clazz); List<Predicate> predicates = new
		 * ArrayList<Predicate>(); if (criteriaPopulator != null) {
		 * criteriaPopulator.populateCriteria(cb, cq, predicates); }
		 * 
		 * Predicate[] array = new Predicate[predicates.size()]; array =
		 * predicates.toArray(array); cq.where(cb.and(array));
		 * 
		 * Path<U> ageAttr = personEntity.<U> get(column); cq =
		 * cq.select(cb.sum(ageAttr));
		 * 
		 * TypedQuery<U> typedQuery = getMongoTeplate().createQuery(cq); if
		 * (start > 0) { typedQuery.setMaxResults(start);
		 * typedQuery.setFirstResult(end); } return
		 * typedQuery.getSingleResult();
		 */
		return null;

	}

	@Override
	public <T> Long getMaxByColumn(Class<T> clazz, String column) {
		/*
		 * CriteriaBuilder cb = getMongoTeplate().getCriteriaBuilder();
		 * CriteriaQuery<Long> cq = cb.createQuery(Long.class); Root<T>
		 * personEntity = cq.from(clazz); Path<Long> ageAttr =
		 * personEntity.<Long> get(column); cq = cq.select(cb.max(ageAttr));
		 * TypedQuery<Long> typedQuery = getMongoTeplate().createQuery(cq); Long
		 * number = typedQuery.getSingleResult(); if (number == null) { return
		 * new Long(0); } else return number;
		 */

		return null;

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
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad) {
		return getPartialModelListWithJoin(clazz, criteriaPopulator,
				firstResult, maxResult, orderBy, false,
				joinTableWithFieldsToLoad, fieldsToLoad);

	}

	@Override
	public <T> List<T> getPartialModelListWithJoin(Class<T> clazz,
			CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, boolean distinct,
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad) {
			Query query = new Query();
			if (criteriaPopulator != null) {
				SpringMongoDBCriteriaHelper<T> mongoDb = new SpringMongoDBCriteriaHelper<>();
				mongoDb.setQuery(query);
				criteriaPopulator.populateCriteria(mongoDb);
			}
			query.skip(firstResult);
			query.limit(firstResult+maxResult);
			return getMongoTeplate().find(query, clazz);
		/*
		 * CriteriaBuilder cb = getMongoTeplate().getCriteriaBuilder();
		 * CriteriaQuery<T> cq = cb.createQuery(clazz); Root<T> r =
		 * cq.from(clazz); List<Predicate> predicates = new
		 * ArrayList<Predicate>(); if (criteriaPopulator != null) {
		 * criteriaPopulator.populateCriteria(cb, cq, predicates); } Predicate[]
		 * array = new Predicate[predicates.size()]; array =
		 * predicates.toArray(array); cq.where(cb.and(array));
		 * 
		 * List<Selection<?>> selections = new ArrayList<Selection<?>>(); if
		 * (fieldsToLoad != null) { for (String fieldToLoad : fieldsToLoad) {
		 * selections.add(r.get(fieldToLoad));
		 * 
		 * } } if (joinTableWithFieldsToLoad != null) { for (String joinTable :
		 * joinTableWithFieldsToLoad.keySet()) {
		 * 
		 * @SuppressWarnings("unchecked") Join<Object, Object> join =
		 * (Join<Object, Object>) r.fetch( joinTable, JoinType.LEFT);
		 * 
		 * for (String fieldsToLoad2 : joinTableWithFieldsToLoad
		 * .get(joinTable)) {
		 * 
		 * selections.add(join.get(fieldsToLoad2)); } } } if
		 * (!selections.isEmpty()) { cq.multiselect(selections); } if (orderBy
		 * == null) { cq.orderBy(cb.desc(r.get("id"))); } else { for
		 * (Map.Entry<String, Boolean> order : orderBy.entrySet()) { if
		 * (order.getValue() == false) {
		 * cq.orderBy(cb.desc(r.get(order.getKey()))); } else {
		 * cq.orderBy(cb.asc(r.get(order.getKey()))); }
		 * 
		 * } } if(distinct) { cq.distinct(true); } TypedQuery<T> typedQuery =
		 * getMongoTeplate().createQuery(cq); if (maxResult > 0) {
		 * typedQuery.setMaxResults(maxResult);
		 * typedQuery.setFirstResult(firstResult); }
		 * 
		 * return typedQuery.getResultList();
		 */
	}

	@Override
	public <T> List<T> getModelList(Class<T> clazz, String filterFieldName,
			Object filterFieldValue) {
		/*
		 * CriteriaBuilder cb = getMongoTeplate().getCriteriaBuilder();
		 * CriteriaQuery<T> cq = cb.createQuery(clazz); Root<T> r =
		 * cq.from(clazz); if (filterFieldValue == null) {
		 * cq.where(cb.isNull(r.get(filterFieldName))); } else {
		 * cq.where(cb.equal(r.get(filterFieldName), filterFieldValue)); }
		 * cq.orderBy(cb.desc(r.get("id"))); return
		 * getMongoTeplate().createQuery(cq).getResultList();
		 */
		return null;
	}

	@Override
	public <T> T getModel(Class<T> clazz, String filterFieldName,
			Object filterFieldValue) {
		/*
		 * CriteriaBuilder cb = getMongoTeplate().getCriteriaBuilder();
		 * CriteriaQuery<T> cq = cb.createQuery(clazz); Root<T> r =
		 * cq.from(clazz); if (filterFieldValue == null) {
		 * cq.where(cb.isNull(r.get(filterFieldName))); } else {
		 * cq.where(cb.equal(r.get(filterFieldName), filterFieldValue)); }
		 * return getMongoTeplate().createQuery(cq).getSingleResult();
		 */
		return null;
	}

	@Override
	public <T> T merge(T model) {
		getMongoTeplate().save(model);
		return model;
	}

	@Override
	public <T> void persist(T model) {
		getMongoTeplate().save(model);
	}

	@Override
	public <T> void refresh(T model) {
		/*
		 * if (!getMongoTeplate().contains(model)) {
		 * getMongoTeplate().merge(model); } getMongoTeplate().refresh(model);
		 */
	}

	@Override
	public <T> void persist(T model, boolean clear) {
		/*
		 * if (clear) getMongoTeplate().clear();
		 */
		getMongoTeplate().save(model);
	}

	@Override
	public <T> void delete(T model) {
		getMongoTeplate().remove(model);
	}

	@Override
	public <T> T getModel(Object id, Class<T> classType) {
		T t = getMongoTeplate().findById(id, classType);
		return t;
	}

	@Override
	public <T> T getModelWithDepth(Object id, Class<T> type,
			String... fetchRelations) {
		/*
		 * CriteriaBuilder criteriaBuilder = getMongoTeplate()
		 * .getCriteriaBuilder(); CriteriaQuery<T> criteriaQuery =
		 * criteriaBuilder.createQuery(type); Root<T> root =
		 * criteriaQuery.from(type);
		 * 
		 * for (String relation : fetchRelations) { FetchParent<T, T> fetch =
		 * root; for (String pathSegment : relation.split("\\.")) { fetch =
		 * fetch.fetch(pathSegment, JoinType.LEFT);
		 * 
		 * } }
		 * 
		 * criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
		 * 
		 * return
		 * getMongoTeplate().createQuery(criteriaQuery).getSingleResult();
		 */
		return null;
	}

	@Override
	public <T> void deleteDetached(T model) {
		getMongoTeplate().remove(model);

	}
	
	

	@Override
	public <T> void delete(Object id, Class<T> classType) {
		// TODO Auto-generated method stub
		
	}

	public abstract MongoTemplate getMongoTeplate();

}
