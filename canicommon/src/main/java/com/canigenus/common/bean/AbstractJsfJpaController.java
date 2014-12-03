package com.canigenus.common.bean;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import org.primefaces.model.LazyDataModel;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.util.CriteriaPopulator;
import com.canigenus.common.util.JPADataModelForPrimeFaces;
import com.canigenus.common.util.JpaCriteriaHelper;

public abstract class AbstractJsfJpaController<T extends Identifiable<?>> extends GenericJsfSearchController<T> {

	
	protected LazyDataModel<T> items = null;

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void search() {

		items = new JPADataModelForPrimeFaces<T>(getService(),
				new CriteriaPopulator<JpaCriteriaHelper<T>>() {

					@Override
					public void populateCriteria(JpaCriteriaHelper<T> jpaCriteriaHelper) {
						AbstractJsfJpaController.this.populateCriteria(jpaCriteriaHelper.getCriteriaBuilder(), jpaCriteriaHelper.getCriteriaQuery(), jpaCriteriaHelper.getPredicates());

					}
				}, getClassType(), null);

	}

	protected abstract void populateCriteria(CriteriaBuilder criteriaBuilder,
			CriteriaQuery<T> criteriaQuery, List<Predicate> predicates) ;

	public LazyDataModel<T> getItems() {
		return items;
	}

	public void setItems(LazyDataModel<T> items) {
		this.items = items;
	}
	
	

}
