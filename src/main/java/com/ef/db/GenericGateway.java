package com.ef.db;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericGateway {
	@Autowired
	private SessionFactory sessionFactory;
	
	public void save(Object object) {
		sessionFactory.getCurrentSession().save(object);
	}
}
