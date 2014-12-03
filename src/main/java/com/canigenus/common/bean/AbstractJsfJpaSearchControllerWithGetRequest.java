package com.canigenus.common.bean;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import com.canigenus.common.model.Identifiable;
import com.canigenus.common.model.LazyDataModel;
import com.canigenus.common.util.CriteriaPopulator;
import com.canigenus.common.util.JpaCriteriaHelper;

public abstract class AbstractJsfJpaSearchControllerWithGetRequest<T extends Identifiable<?>> extends GenericJsfSearchController<T> {

	
	protected LazyDataModel<T> items = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void search(){
		
		items =new LazyDataModel<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<T> load(Integer first, Integer pageSize,
					String sortField, Boolean sortOrder) {
				// TODO Auto-generated method stub
				return getService().getPartialModelList(getClassType(), new CriteriaPopulator<JpaCriteriaHelper<T>>() {

					@Override
					public void populateCriteria(JpaCriteriaHelper<T> jpaCriteriaHelper) {
						AbstractJsfJpaSearchControllerWithGetRequest.this.populateCriteria(jpaCriteriaHelper.getCriteriaBuilder(), jpaCriteriaHelper.getCriteriaQuery(), jpaCriteriaHelper.getPredicates());
						
					}

					
				},
						first, pageSize);
			}

			@Override
			public Long count() {
				// TODO Auto-generated method stub
				return getService().getModelCount(getClassType(), new CriteriaPopulator<JpaCriteriaHelper<T>>() {

					@Override
					public void populateCriteria(JpaCriteriaHelper<T> jpaCriteriaHelper) {
						AbstractJsfJpaSearchControllerWithGetRequest.this.populateCriteria(jpaCriteriaHelper.getCriteriaBuilder(), jpaCriteriaHelper.getCriteriaQuery(), jpaCriteriaHelper.getPredicates());

					}
				});
			}
		};
		if(page!=null)
		{
		items.setCurrentPage(page);
		}

	}
	
	public abstract void populateCriteria(CriteriaBuilder cb, CriteriaQuery<?> cq,
			List<Predicate> predicates) ;

	public LazyDataModel<T> getItems() {
		return items;
	}

	public void setItems(LazyDataModel<T> items) {
		this.items = items;
	}


	
	 
	
}
