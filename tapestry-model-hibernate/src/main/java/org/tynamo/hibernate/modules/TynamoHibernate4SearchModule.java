package org.tynamo.hibernate.modules;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.PropertyShadowBuilder;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.hibernate.services.internal.SearchableHibernateGridDataSourceProvider;
import org.tynamo.hibernate.decorators.HibernateSearchDescriptorDecorator;
import org.tynamo.services.SearchableGridDataSourceProvider;

@SubModule(TynamoHibernate4Module.class)
public final class TynamoHibernate4SearchModule
{

	public static void bind(ServiceBinder binder)
	{
		binder.bind(SearchableGridDataSourceProvider.class, SearchableHibernateGridDataSourceProvider.class).withId("SearchableHibernateGridDataSourceProvider");
	}

	@Contribute(ServiceOverride.class)
	public static void setupApplicationServiceOverrides(MappedConfiguration<Class, Object> configuration,
	                                                    @Local SearchableGridDataSourceProvider dataSourceProvider)
	{
		configuration.add(SearchableGridDataSourceProvider.class, dataSourceProvider);
	}

	@Contribute(DescriptorFactory.class)
	public static void setupDescriptorFactory(OrderedConfiguration<DescriptorDecorator> configuration)
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
