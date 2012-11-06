package org.tynamo.model.jpa;


public class TynamoJpaSymbols
{

	/**
	 * Columns longer than this will have their large property set to true.
	 */
	public static final String LARGE_COLUMN_LENGTH = "tynamo.model.jpa.large-column-length";

	/**
	 * When working with objects from multiple sources jpa decorator complains about "metadata not found",
	 * this symbol configured to true tells JPADescriptorDecorator to ignore these errors.
	 */
	public static final String IGNORE_NON_HIBERNATE_TYPES = "tynamo.model.jpa.ignore-non-jpa-types";

	/**
	 * type string, default "" (if unit name is empty us the configured persistence unit if there's only a single one)
	 */
	public static final String PERSISTENCEUNIT = "tynamo.model.jpa.persistenceunit";

	public static final String ELASTICSEARCH_HOME = "elasticsearch.path.home"; // i.e. elasticsearch path.home property

	public static final String ELASTICSEARCH_HTTP_ENABLED = "elasticsearch.http.enabled"; // i.e. elasticsearch http.enabled property

}
