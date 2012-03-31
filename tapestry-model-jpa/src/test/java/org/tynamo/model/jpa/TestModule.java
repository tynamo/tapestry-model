package org.tynamo.model.jpa;

import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.InternalSymbols;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.LoggerSource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.jpa.JpaEntityPackageManager;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.model.jpa.internal.ConfigurableEntityManagerProvider;
import org.tynamo.model.jpa.services.JpaDescriptorDecorator;
import org.tynamo.model.test.entities.Foo;

public class TestModule
{

	@Contribute(SymbolProvider.class)
	@ApplicationDefaults
	public static void defaultsSymbols(MappedConfiguration<String, String> configuration)
	{
		configuration.add(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM, "org.tynamo.model.jpa");
		configuration.add(InternalSymbols.APP_PACKAGE_PATH, "org/tynamo/model/jpa");

		// JpaDescriptorDecoratorTest doesn't include TynamoJpaModule at all, but JpaDescriptorDecorator expects persistenceunit to be configured 
		configuration.add(TynamoJpaSymbols.LARGE_COLUMN_LENGTH, "100");
		configuration.add(TynamoJpaSymbols.IGNORE_NON_HIBERNATE_TYPES, "true");

		configuration.add(TynamoJpaSymbols.PERSISTENCEUNIT, "");
	}

	@Contribute(JpaEntityPackageManager.class)
	public static void addPackages(Configuration<String> configuration)
	{
		configuration.add(Foo.class.getPackage().getName());
	}

	public static JpaDescriptorDecorator buildJPADescriptorDecorator(@Autobuild ConfigurableEntityManagerProvider entityManagerProvider,
	                                                               DescriptorFactory descriptorFactory,
	                                                               @Symbol(TynamoJpaSymbols.LARGE_COLUMN_LENGTH)
	                                                               int largeColumnLength,
	                                                               @Symbol(TynamoJpaSymbols.IGNORE_NON_HIBERNATE_TYPES)
	                                                               boolean ignoreNonHibernateTypes,
	                                                               LoggerSource loggerSource)
	{
		return new JpaDescriptorDecorator(
				descriptorFactory, entityManagerProvider,
				largeColumnLength,
				ignoreNonHibernateTypes,
				loggerSource.getLogger(JpaDescriptorDecorator.class));
	}
}
