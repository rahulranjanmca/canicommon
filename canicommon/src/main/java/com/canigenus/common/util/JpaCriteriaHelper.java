package com.canigenus.common.util;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public class JpaCriteriaHelper<T> {
	
	 CriteriaBuilder criteriaBuilder;
	 CriteriaQuery<T> criteriaQuery;
	 List<Predicate> predicates;
	
	 public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}
	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}
	public CriteriaQuery<T> getCriteriaQuery() {
		return criteriaQuery;
	}
	@SuppressWarnings("unchecked")
	public void setCriteriaQuery(CriteriaQuery<?> criteriaQuery) {
		this.criteriaQuery = (CriteriaQuery<T>)criteriaQuery;
	}
	public List<Predicate> getPredicates() {
		return predicates;
	}
	public void setPredicates(List<Predicate> predicates) {
		this.predicates = predicates;
	}
	 
	 

}
