package org.tynamo.hibernate.modules;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateCore;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.tynamo.hibernate.v4.Hibernate4SessionManagerImpl;
import org.tynamo.hibernate.v4.Hibernate4SessionSourceImpl;
import org.tynamo.hibernate.v4.PackageNameHibernate4Configurer;

public final class TynamoHibernate4Module
{

	public static void bind(ServiceBinder binder)
	{
		binder.bind(HibernateSessionSource.class, Hibernate4SessionSourceImpl.class).withId("Hibernate4SessionSource");
	}

	@Contribute(ServiceOverride.class)
	public static void sessionSourceForHibernate4(MappedConfiguration<Class, Object> configuration,
	                                              @Local HibernateSessionSource hibernateSessionSource,
	                                              @Local HibernateSessionManager hibernateSessionManager)
	{
		configuration.add(HibernateSessionSource.class, hibernateSessionSource);
		configuration.add(HibernateSessionManager.class, hibernateSessionManager);
	}

	/**
	 * The session manager manages sessions on a per-thread/per-request basis. Any active transaction will be rolled
	 * back at {@linkplain org.apache.tapestry5.ioc.Registry#cleanupThread() thread cleanup time}.  The thread is
	 * cleaned up automatically in a Tapestry web application.
	 */
	@Scope(ScopeConstants.PERTHREAD)
	@ServiceId("Hibernate4SessionManager")
	public static HibernateSessionManager buildHibernateSessionManager(@Local HibernateSessionSource sessionSource,
	                                                                   PerthreadManager perthreadManager)
	{
		Hibernate4SessionManagerImpl service = new Hibernate4SessionManagerImpl(sessionSource);

		perthreadManager.addThreadCleanupListener(service);

		return service;
	}

	/**
	 * Adds the following configurers: <dl> <dt>Default <dd> performs default hibernate configuration <dt>PackageName
	 * <dd> loads entities by package name</dl>
	 */
	@Local
	@Contribute(HibernateSessionSource.class)
	public static void hibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config,
	                                          @HibernateCore HibernateConfigurer defaultHibernateConfigurer)
	{
		config.add("Default", defaultHibernateConfigurer);
		config.addInstance("PackageName", PackageNameHibernate4Configurer.class);
	}
}
