package ${package}.pages;


import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.builder.BuilderDirector;
import org.trailsframework.descriptor.TrailsClassDescriptor;
import org.trailsframework.hibernate.pages.HibernateModelPage;

public class New extends HibernateModelPage
{

	private static final Logger LOGGER = LoggerFactory.getLogger(New.class);

	@Inject
	private BuilderDirector builderDirector;

	private TrailsClassDescriptor classDescriptor;

	@Property(write = false)
	private BeanModel beanModel;

	private Object bean;

	public TrailsClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public Object getBean()
	{
		return bean;
	}

	void pageLoaded()
	{
		// Make other changes to _model here.
	}

	void onActivate(Class clazz) throws Exception
	{
		bean = builderDirector.createNewInstance(clazz);
		classDescriptor = getDescriptorService().getClassDescriptor(clazz);
		beanModel = getBeanModelSource().create(clazz, true, getMessages());
//		BeanModelUtils.modify(_beanModel, null, null, null, null);
	}

	/**
	 * This tells Tapestry to put _personId into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	Object[] onPassivate()
	{
		return new Object[]{classDescriptor.getType()};
	}

	void cleanupRender()
	{
		bean = null;
		classDescriptor = null;
		beanModel = null;
	}


	public Link back()
	{
		return getResources().createPageLink(List.class, false, getClassDescriptor().getType());
	}
}