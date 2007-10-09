package org.trails.record;

import org.apache.tapestry.record.PropertyPersistenceStrategy;


public interface IClientAsoPropertyPersistenceStrategy extends PropertyPersistenceStrategy
{
	void addPropertyName(String propertyName);
}
