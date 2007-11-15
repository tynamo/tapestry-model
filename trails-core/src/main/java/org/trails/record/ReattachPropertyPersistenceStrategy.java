package org.trails.record;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.record.PropertyChange;
import org.apache.tapestry.record.PropertyChangeImpl;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class is a Trails adaptation of Tapernate's ReattachPropertyPersistenceStrategy
 * Credits to James Carman.
 */
public abstract class ReattachPropertyPersistenceStrategy extends AbstractSessionPropertyPersistenceStrategy
{

	private static final Log LOG = LogFactory.getLog(ReattachPropertyPersistenceStrategy.class);

	public Collection getStoredChanges(String pageName)
	{
		final Collection<PropertyChange> pageChanges = super.getStoredChanges(pageName);
		Collection<PropertyChange> result = new ArrayList<PropertyChange>();

		for (PropertyChange propertyChange : pageChanges)
		{
			final Object entity = propertyChange.getNewValue();
			LOG.debug("Reattaching property " + propertyChange.getPropertyName() + " on component " + propertyChange.getComponentPath());

			try
			{
				result.add(new PropertyChangeImpl(propertyChange.getComponentPath(), propertyChange.getPropertyName(), reattach(entity)));
			} catch (Exception e)
			{
				e.printStackTrace();  //@todo throw a proper Exception
			}
		}
		return result;
	}
}