package org.tynamo.hibernate;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.hibernate.cfg.Configuration;

public class TynamoInterceptorConfigurer implements HibernateConfigurer
{
	private final TynamoInterceptor interceptor;

	public TynamoInterceptorConfigurer(TynamoInterceptor interceptor)
	{
		this.interceptor = interceptor;
	}

	public void configure(Configuration configuration)
	{
		configuration.setInterceptor(interceptor);
	}
}
