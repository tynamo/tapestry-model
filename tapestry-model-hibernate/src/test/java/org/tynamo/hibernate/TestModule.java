package org.tynamo.hibernate;

import org.apache.tapestry5.hibernate.HibernateEntityPackageManager;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.InternalSymbols;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

public class TestModule
{

	@Contribute(SymbolProvider.class)
	@ApplicationDefaults
	public static void defaultsSymbols(MappedConfiguration<String, String> configuration)
	{
		configuration.add(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM, "org.tynamo.hibernate");
		configuration.add(InternalSymbols.APP_PACKAGE_PATH, "org/tynamo/hibernate");

		configuration.add(TynamoHibernateSymbols.LARGE_COLUMN_LENGTH, "100");
		configuration.add(TynamoHibernateSymbols.IGNORE_NON_HIBERNATE_TYPES, "true");
	}

	@Contribute(HibernateEntityPackageManager.class)
	public static void addPackages(Configuration<String> configuration)
	{
		configuration.add("org.tynamo.testhibernate");
	}

}
