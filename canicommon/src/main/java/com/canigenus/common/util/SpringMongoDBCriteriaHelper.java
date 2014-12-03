package com.canigenus.common.util;

import org.springframework.data.mongodb.core.query.Query;



public class SpringMongoDBCriteriaHelper<T> {
	
	 Query query;

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
	 
	 
	 
}
