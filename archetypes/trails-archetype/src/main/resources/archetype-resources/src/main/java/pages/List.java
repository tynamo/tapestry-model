package ${package}.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.descriptor.TrailsClassDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.DisplayNameUtils;
import org.trailsframework.util.Identifiable;

public class List
{

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Messages messages;

	@Property
	private Identifiable model;

	@Property(write = false)
	private TrailsClassDescriptor classDescriptor;

	void onActivate(Class clazz) throws Exception
	{
		classDescriptor = descriptorService.getClassDescriptor(clazz);
	}

	Object[] onPassivate()
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
		return messages.format("org.trails.i18n.list", DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

	public String getNewLinkMessage()
	{
		return messages.format("org.trails.i18n.new", DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

}
