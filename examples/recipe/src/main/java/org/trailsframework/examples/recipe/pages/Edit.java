package org.trailsframework.examples.recipe.pages;


import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.DisplayNameUtils;
import org.trailsframework.validation.HibernateClassValidatorFactory;
import org.trailsframework.validation.HibernateValidationDelegate;
import org.hibernate.validator.InvalidStateException;

public class Edit
{

	private static final Logger LOGGER = LoggerFactory.getLogger(Edit.class);

	@Inject
	private ContextValueEncoder contextValueEncoder;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private HibernateClassValidatorFactory hibernateClassValidatorFactory;

	@Inject
	private HibernateValidationDelegate hibernateValidationDelegate;

	@Inject
	private ComponentResources resources;

	@Property(write = false)
	private IClassDescriptor classDescriptor;

	@Property(write = false)
	private BeanModel beanModel;

	@Property(write = false)
	private Object bean;

	@Component
	private Form form;

	@Component
	private PageLink link;

	void pageLoaded()
	{
		// Make other changes to the bean here.
	}

	void onActivate(Class clazz, String id) throws Exception
	{
		bean = contextValueEncoder.toValue(clazz, id);
		classDescriptor = descriptorService.getClassDescriptor(clazz);
		beanModel = beanModelSource.create(clazz, true, messages);
//		BeanModelUtils.modify(_beanModel, null, null, null, null);
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	Object[] onPassivate()
	{
		return new Object[]{classDescriptor.getType(), bean};
	}

	void onValidateFormFromForm() throws ValidationException
	{
		LOGGER.debug("validating");
		//add more validation logic here
		try
		{
			/**
			 * The hibernate validate listener is enabled by default, so if nothing is wrong this entity will be
			 * validated twice, once here, and once in session.saveOrUpdate(instance);
			 */
			hibernateClassValidatorFactory.validateEntity(bean);
		} catch (InvalidStateException ise)
		{
			hibernateValidationDelegate.record(classDescriptor, ise, form.getDefaultTracker(), messages);
		}
	}

	Object onSuccess()
	{
		try
		{

			LOGGER.debug("saving....");
			persitenceService.save(bean);
			return backToList();

		} catch (InvalidStateException ise)
		{
			hibernateValidationDelegate.record(classDescriptor, ise, form.getDefaultTracker(), messages);
		} catch (Exception e)
		{
//			missing ExceptionUtils (Lang 2.3 API)
//			form.recordError(ExceptionUtil.getRootCause(e));
			form.recordError(e.getMessage());
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	void cleanupRender()
	{
		bean = null;
		classDescriptor = null;
		beanModel = null;
	}

	public Link onActionFromDelete()
	{
		persitenceService.remove(bean);
		return backToList();
	}

	public Link onActionFromCancel()
	{
		return backToList();
	}

	public Link backToList()
	{
		return resources.createPageLink(List.class, false, classDescriptor.getType());
	}

	public String getTitle()
	{
		return messages.format("org.trails.i18n.edit", DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public String getListAllLinkMessage()
	{
		return messages.format("org.trails.component.listalllink", DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

}
