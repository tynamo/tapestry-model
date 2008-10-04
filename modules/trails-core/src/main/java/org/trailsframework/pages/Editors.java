package org.trailsframework.pages;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.GenericSelectionModel;


public class Editors
{
	@Environmental
	private PropertyEditContext context;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	public PropertyEditContext getContext()
	{
		return context;
	}

	public SelectModel getSelectModel()
	{
		return new GenericSelectionModel(persitenceService.getInstances(context.getPropertyType()));
	}

	/**
	 * Provide a value encoder for an enum type.
	 *
	 * @return
	 */
	public ValueEncoder getValueEncoderForProperty()
	{
		return valueEncoderSource.getValueEncoder(context.getPropertyType());
	}
}
