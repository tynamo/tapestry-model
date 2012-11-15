package org.tynamo.hibernate.modules;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyShadowBuilder;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.hibernate.decorators.HibernateSearchDescriptorDecorator;

@SubModule(TynamoHibernate4Module.class)
public final class TynamoHibernate4SearchModule
{

	public static void contributeDescriptorFactory(OrderedConfiguration<DescriptorDecorator> configuration,
	                                               PropertyAccess propertyAccess, ObjectLocator locator)
	{
		configuration.add("SearchDecorator", new HibernateSearchDescriptorDecorator(), "after:TynamoDecorator");
	}

	public static FullTextSession buildFullTextSession(HibernateSessionManager sessionManager,
	                                                   PropertyShadowBuilder propertyShadowBuilder)

	{
		LazyFullTextSession lazy = new LazyFullTextSession(sessionManager);
		return propertyShadowBuilder.build(lazy, "fullTextSession", FullTextSession.class);
	}

	public static class LazyFullTextSession
	{
		private HibernateSessionManager sessionManager;

		public LazyFullTextSession(HibernateSessionManager sessionManager)
		{
			this.sessionManager = sessionManager;
		}

		public FullTextSession getFullTextSession()
		{
			return Search.getFullTextSession(sessionManager.getSession());
		}
	}
}
