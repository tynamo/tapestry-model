package org.tynamo.hibernate;


public class TrailsHibernateSymbols
{

	/**
	 * Columns longer than this will have their large property set to true.
	 */
	public static final String LARGE_COLUMN_LENGTH = "trails.hibernate.large-column-length";

	/**
	 * When working with objects from multiple sources hibernate decorator complains about "metadata not found",
	 * this symbol configured to true tells HibernateDescriptorDecorator to ignore these errors.
	 */
	public static final String IGNORE_NON_HIBERNATE_TYPES = "trails.hibernate.ignore-non-hibernate-types";

}
