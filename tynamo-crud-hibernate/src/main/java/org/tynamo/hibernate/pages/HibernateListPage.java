package org.tynamo.hibernate.pages;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.DisplayNameUtils;

public class HibernateListPage
{
	final static ToStringStyle commaSeparatedListStyle = new StandardToStringStyle();

	static
	{
		StandardToStringStyle styleValues = (StandardToStringStyle) HibernateListPage.commaSeparatedListStyle;

		styleValues.setUseClassName(false);
		styleValues.setUseIdentityHashCode(false);
		styleValues.setUseFieldNames(false);
		styleValues.setContentStart("");
		styleValues.setContentEnd("");
		styleValues.setArrayStart("");
		styleValues.setArrayEnd("");
		styleValues.setArraySeparator(",");
	}

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Messages messages;

	@Inject
	private PropertyAccess propertyAccess;

	@Property
	private Object model;

	@Property(write = false)
	private TrailsClassDescriptor classDescriptor;

	protected void onActivate(Class clazz) throws Exception
	{
		classDescriptor = descriptorService.getClassDescriptor(clazz);
	}

	protected Object[] onPassivate()
	{
		return new Object[]{classDescriptor.getType()};
	}

	public java.util.List getInstances()
	{
		return persitenceService.getInstances(classDescriptor.getType());
	}

	public Object[] getEditPageContext()
	{
		return new Object[]{classDescriptor.getType(), model};
	}

	public String getTitle()
	{
		return messages.format("org.tynamo.i18n.list", DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

	public String getNewLinkMessage()
	{
		return messages.format("org.tynamo.i18n.new", DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public final String getModelId()
	{
		return propertyAccess.get(model, classDescriptor.getIdentifierDescriptor().getName()).toString();
	}

	/**
	 * @return A comma-separated list of property names to be removed from the Grid BeanModel
	 */
	public String getExcludeList()
	{
		try
		{
			/* I'm sorry about this code, but it was the fastest way */
			java.util.List<String> names = (java.util.List<String>) Ognl
					.getValue("propertyDescriptors.{? identifier or !summary or hidden}.{name}", classDescriptor);

			return new ToStringBuilder(names, commaSeparatedListStyle).append(names.toArray()).toString();

		} catch (OgnlException e)
		{
		}
		
		return "";
	}
}
