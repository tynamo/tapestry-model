package org.tynamo.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Heartbeat;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.DisplayNameUtils;

import java.util.Collection;
import java.util.List;

public class Composition
{

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

	@Inject
	private BuilderDirector builderDirector;

	@Inject
	@Property
	private ComponentResources resources;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Inject
	private Request request;

	@Environmental
	private Heartbeat heartbeat;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness. The uniqued value may be accessed via the
	 * {@link #getClientId() clientId property}.
	 */
	@Parameter(value = "prop:resources.id", defaultPrefix = BindingConstants.LITERAL)
	@Property(write = false)
	private String clientId;

	/**
	 * A Block to render when the source is empty. The default is simply the text "There is no data to display".
	 * This parameter is used to customize that message.
	 */
	@Parameter(value = "block:empty", defaultPrefix = BindingConstants.LITERAL)
	@Property(write = false)
	private Block empty;

	@Parameter(required = false)
	private List instances;

	@Parameter(required = true)
	@Property(write = false)
	private Collection collection;

	/**
	 * The object which owns the collection being edited
	 */
	@Parameter(required = true)
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

	@Property
	private Object formBean;

	@Parameter(value = "true")
	private boolean allowCreate;

	@InjectComponent
	private Zone compositionZone;

	@InjectComponent
	@Property(write = false)
	private Form form;

	@Inject
	private TypeCoercer typeCoercer;

	public boolean isAllowCreate()
	{
		return propertyAccess.get(owner, descriptorService.getClassDescriptor(owner.getClass())
				.getIdentifierDescriptor().getName()) != null;
	}

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

	/**
	 * The image to use for the delete icon
	 */
	@Parameter(value = "asset:delete.png")
	@Property(write = false)
	private Asset deleteIcon;

	@Property(write = false)
	private BeanModel beanModel;

	private Element addLink;

	void onPrepareFromForm()
	{
		resources.triggerEvent(EventConstants.PREPARE, null, null);

		if (formBean == null) {
			formBean = builderDirector.createNewInstance(collectionDescriptor.getElementType());
		}

		if (beanModel == null)
		{
			beanModel = beanModelSource.createEditModel(collectionDescriptor.getElementType(), messages);
		}
	}

	void setupRender()
	{
		beanModel = beanModelSource.createEditModel(collectionDescriptor.getElementType(), messages);
		formBean = builderDirector.createNewInstance(collectionDescriptor.getElementType());
	}

	boolean beginRender(MarkupWriter writer)
	{

		writer.element("div", "class", "t-add-child");
		addLink = writer.element("a", "id", "add-" + collectionDescriptor.getName() + "-link", "href", "#", "class", "t-add-child-link");
		writer.write(messages.get("org.tynamo.i18n.add-child"));

		Runnable command = new Runnable()
		{
			public void run()
			{
				String fieldId = form.getClientId();
				addLink.forceAttributes("onclick", "Element.toggle('" + form.getClientId() + "'); this.blur(); return false;");
			}
		};
		heartbeat.defer(command);

		writer.end();
		writer.end();

		return true;
	}

	public Object onSuccess()
	{
		persitenceService.addToCollection(collectionDescriptor, formBean, owner);

		if (request.isXHR()) return compositionZone.getBody();
		else return null;
	}

	public Object onActionFromDeleteChild(String elementid)
	{
		ValueEncoder valueEncoder = valueEncoderSource.getValueEncoder(collectionDescriptor.getElementType());
		Object element = valueEncoder.toValue(elementid);

		persitenceService.removeFromCollection(collectionDescriptor, element, owner);

		if (request.isXHR())
		{
			return compositionZone.getBody();
		}

		return null;
	}


/*	public boolean isList()
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

	public Object[] getShowPageContext()
	{
		return new Object[]{collectionIterator.getClass(), collectionIterator};
	}

	public Object[] getDeleteContext()
	{
		return new Object[]{collectionIterator};
	}

	public String getLegendMessage()
	{
		return messages.format("org.tynamo.i18n.add",
				DisplayNameUtils.getDisplayName(collectionDescriptor.getElementType(), messages));
	}

	public String getCompositionZoneClientId()
	{
		return compositionZone.getClientId();
	}

}