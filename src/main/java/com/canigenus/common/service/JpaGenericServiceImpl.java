package com.canigenus.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
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

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.util.CriteriaPopulator;
import com.canigenus.common.util.JpaCriteriaHelper;

/**
 * 
 */

/**
 * @author RanjaRah
 * 
 */
public abstract class JpaGenericServiceImpl implements GenericService {

	private static final long serialVersionUID = 1L;

	@Override
	public <T> T getPartialModel(Class<T> clazz, Object id,
			String... fieldsToLoad) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
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
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public <T> boolean isUnique(Class<T> entity, String propertyName,
			Object propertyValue) {
		if (entity == null || propertyValue == null) {
			return true;
		}
		return getEntityManager()
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
		return getEntityManager()
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
		CriteriaBuilder criteriaBuilder = getEntityManager()
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
		return getEntityManager().createQuery(criteriaQuery).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Long getModelCount(Class<T> clazz,
			@SuppressWarnings("rawtypes") CriteriaPopulator criteriaPopulator) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(clazz)));
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (criteriaPopulator != null) {
			JpaCriteriaHelper<?> jpaCriteriaHelper= new JpaCriteriaHelper<>();
			jpaCriteriaHelper.setCriteriaBuilder(cb);
			jpaCriteriaHelper.setCriteriaQuery(cq);
			jpaCriteriaHelper.setPredicates(predicates);
			criteriaPopulator.populateCriteria(jpaCriteriaHelper);
		}
		Predicate[] array = new Predicate[predicates.size()];
		array = predicates.toArray(array);
		cq.where(cb.and(array));

		return getEntityManager().createQuery(cq).getSingleResult();
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
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<U> cq = cb.createQuery(returningClass);
		Root<T> personEntity = cq.from(clazz);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (criteriaPopulator != null) {
			JpaCriteriaHelper<?> jpaCriteriaHelper= new JpaCriteriaHelper<>();
			jpaCriteriaHelper.setCriteriaBuilder(cb);
			jpaCriteriaHelper.setCriteriaQuery(cq);
			jpaCriteriaHelper.setPredicates(predicates);
			criteriaPopulator.populateCriteria(jpaCriteriaHelper);
		}

		Predicate[] array = new Predicate[predicates.size()];
		array = predicates.toArray(array);
		cq.where(cb.and(array));

		Path<U> ageAttr = personEntity.<U> get(column);
		cq = cq.select(cb.sum(ageAttr));

		TypedQuery<U> typedQuery = getEntityManager().createQuery(cq);
		if (start > 0) {
			typedQuery.setMaxResults(start);
			typedQuery.setFirstResult(end);
		}
		return typedQuery.getSingleResult();

	}

	@Override
	public <T> Long getMaxByColumn(Class<T> clazz, String column) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> personEntity = cq.from(clazz);
		Path<Long> ageAttr = personEntity.<Long> get(column);
		cq = cq.select(cb.max(ageAttr));
		TypedQuery<Long> typedQuery = getEntityManager().createQuery(cq);
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getPartialModelListWithJoin(Class<T> clazz,
			@SuppressWarnings("rawtypes") CriteriaPopulator criteriaPopulator, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, boolean distinct, 
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> r = cq.from(clazz);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (criteriaPopulator != null) {
			JpaCriteriaHelper<?> jpaCriteriaHelper= new JpaCriteriaHelper<>();
			jpaCriteriaHelper.setCriteriaBuilder(cb);
			jpaCriteriaHelper.setCriteriaQuery(cq);
			jpaCriteriaHelper.setPredicates(predicates);
			criteriaPopulator.populateCriteria(jpaCriteriaHelper);
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
		TypedQuery<T> typedQuery = getEntityManager().createQuery(cq);
		if (maxResult > 0) {
			typedQuery.setMaxResults(maxResult);
			typedQuery.setFirstResult(firstResult);
		}

		return typedQuery.getResultList();
	}

	@Override
	public <T> List<T> getModelList(Class<T> clazz, String filterFieldName,
			Object filterFieldValue) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> r = cq.from(clazz);
		if (filterFieldValue == null) {
			cq.where(cb.isNull(r.get(filterFieldName)));
		} else {
			cq.where(cb.equal(r.get(filterFieldName), filterFieldValue));
		}
		cq.orderBy(cb.desc(r.get("id")));
		return getEntityManager().createQuery(cq).getResultList();
	}
	
	@Override
	public <T> T getModel(Class<T> clazz, String filterFieldName,
			Object filterFieldValue) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> r = cq.from(clazz);
		if (filterFieldValue == null) {
			cq.where(cb.isNull(r.get(filterFieldName)));
		} else {
			cq.where(cb.equal(r.get(filterFieldName), filterFieldValue));
		}
		return getEntityManager().createQuery(cq).getSingleResult();
	}


	@Override
	public <T> T merge(T model) {
		return getEntityManager().merge(model);
	}

	@Override
	public <T> void persist(T model) {
		getEntityManager().persist(model);
	}

	@Override
	public <T> void refresh(T model) {
		if (!getEntityManager().contains(model)) {
			getEntityManager().merge(model);
		}
		getEntityManager().refresh(model);
	}

	@Override
	public <T> void persist(T model, boolean clear) {
		if (clear)
			getEntityManager().clear();
		getEntityManager().merge(model);
	}

	@Override
	public <T> void delete(T model) {
		getEntityManager().remove(model);
	}

	@Override
	public <T> T getModel(Object id, Class<T> classType) {
		T t = getEntityManager().find(classType, id);
		return t;
	}

	@Override
	public <T> T getModelWithDepth(Object id, Class<T> type,
			String... fetchRelations) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
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

		return getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}

	@Override
	public <T> void deleteDetached(T model) {
		getEntityManager().remove(getEntityManager().merge(model));

	}
	
	@Override
	public <T> void delete(Object id,  Class<T> classType){}

	public abstract EntityManager getEntityManager();
}
