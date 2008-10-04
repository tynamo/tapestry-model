package org.trailsframework.pages;

import org.trailsframework.util.GenericSelectionModel;
import org.trailsframework.services.PersitenceService;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Environmental;


public class Editors
{
	@Environmental
	private PropertyEditContext context;

	@Inject
	private PersitenceService persitenceService;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	public PropertyEditContext getContext()
	{
		return context;
	}

	public SelectModel getSelectModel()
	{
		return new GenericSelectionModel<Caetg>(persitenceService.getAllInstances(Caetg.class));
	}

	/**
	 * Provide a value encoder for an enum type.
	 */
	public ValueEncoder getValueEncoderForProperty()
	{
		return valueEncoderSource.getValueEncoder(context.getPropertyType());
	}
}
