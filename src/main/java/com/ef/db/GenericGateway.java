package com.ef.db;

import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericGateway {
	@Autowired
	private SessionFactory sessionFactory;
	
	public void save(Object object) {
		sessionFactory.getCurrentSession().save(object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> NativeQuery<T> createSqlQuery(String queryString) {
		return sessionFactory.getCurrentSession().createNativeQuery(queryString);
	}
}
