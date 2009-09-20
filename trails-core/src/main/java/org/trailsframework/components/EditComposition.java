package org.trailsframework.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.BeanModelSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.descriptor.CollectionDescriptor;
import org.trailsframework.descriptor.TrailsClassDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;

import java.util.Collection;
import java.util.List;


/**
 * This component produces a editor for a Composition (child collection).
 * It allows a user to edit a collection property
 */
public class EditComposition
{

	private static final Logger LOGGER = LoggerFactory.getLogger(EditComposition.class);

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private PropertyAccess propertyAccess;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness. The uniqued value may be accessed via the
	 * {@link #getClientId() clientId property}.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	@Property(write = false)
	private String clientId;

	@Parameter(required = false)
	private List instances;

	@Parameter(required = true)
	@Property(write = false)
	private Collection collection;

	/**
	 * The object which owns the collection being edited
	 */
	@Parameter(required = false)
	private Object owner;

	/**
	 * Ognl expression to invoke on the model to create a new child instance
	 */
	@Parameter(required = false)
	private String createExpression;

	/**
	 * @return The CollectionDescriptor for the collection being edited
	 */
	@Parameter(required = true)
	@Property(write = false)
	private CollectionDescriptor collectionDescriptor;

	@Property
	private Object collectionIterator;

	@Parameter(value = "true")
	private boolean allowCreate;

	@Parameter(value = "prop:collectionDescriptor.allowRemove")
	private boolean allowRemove;

	@Property
	private int index;

	@Parameter(value = "asset:move_up.gif")
	@Property(write = false)
	private Asset upImage;

	@Parameter(value = "asset:move_down.gif")
	@Property(write = false)
	private Asset downImage;

	@Property(write = false)
	private BeanModel beanModel;

	void setupRender()
	{
		beanModel = beanModelSource.create(collectionDescriptor.getElementType(), false, messages);
		for (String propertyName : (List<String>) beanModel.getPropertyNames())
		{
			beanModel.get(propertyName).sortable(false);
		}
	}

/*	public void remove()
	{
		int i = 0;
		// TODO CN - This code stinks (I wrote it).  Isn't there a better way??
		ArrayList deleting = new ArrayList();
		for (Object element : getCollection())
		{
			if (getSelected().get(i))
			{
				deleting.add(element);
			}
			i++;
		}

		for (Object element : deleting)
		{
			Utils.executeOgnlExpression(collectionDescriptor.getRemoveExpression(), element, owner);
			persitenceService.remove(element);
		}
	}

	public boolean isList()
	{
		return collection instanceof List;
	}

	public void moveUp()
	{
		List list = (List) collection;
		for (int i = 1; i < getSelected().size(); i++)
		{
			if (getSelected().get(i))
			{
				if (collectionDescriptor.getSwapExpression() == null)
				{
					Collections.swap(list, i, i - 1);
				} else
				{
					try
					{
						Ognl.getValue(collectionDescriptor.getSwapExpression() + "(" + i + "," + (i - 1) + ")", owner);
					} catch (OgnlException e)
					{
						LOGGER.error(e.getMessage());
					}
				}
			}
		}
	}

	public void moveDown()
	{
		List list = (List) collection;
		for (int i = 0; i < getSelected().size() - 1; i++)
		{
			if (collectionDescriptor.getSwapExpression() == null)
			{
				Collections.swap(list, i, i + 1);
			} else
			{
				try
				{
					Ognl.getValue(collectionDescriptor.getSwapExpression() + "(" + i + "," + (i + 1) + ")",
							owner);
				} catch (OgnlException e)
				{
					LOG.error(e.getMessage());
				}
			}
		}
	}*/

	public Object[] getEditPageContext()
	{
		return new Object[]{collectionDescriptor.getBeanType(), owner, collectionDescriptor.getName(),
				collectionIterator};
	}

	public final String getModelId()
	{
		TrailsClassDescriptor classDescriptor =
				descriptorService.getClassDescriptor(collectionDescriptor.getBeanType());
		return propertyAccess.get(collectionIterator, classDescriptor.getIdentifierDescriptor().getName()).toString();
	}
}
