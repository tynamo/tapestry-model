package org.trails.component;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Lifecycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Insert;
import org.trails.TrailsRuntimeException;
import org.trails.callback.AssociationCallback;
import org.trails.callback.CallbackStack;
import org.trails.callback.EditCallback;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.OwningObjectReferenceDescriptor;
import org.trails.page.EditPage;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage.PageType;
import org.trails.persistence.PersistenceService;
import org.trails.validation.TrailsValidationDelegate;

/**
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 * @OneToOne use case.
 * <p/>
 * This guy manages the owning side user interface of a OneToOne association.
 * <p/>
 * Owner-<>-----Association
 */
@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class AssociationMgt extends TrailsComponent
{
	protected static final Log LOG = LogFactory.getLog(AssociationMgt.class);

	@Bean(lifecycle = Lifecycle.REQUEST)
	public abstract TrailsValidationDelegate getDelegate();

	public abstract String getCreateExpression();

	public abstract void setCreateExpression(String CreateExpression);

	@Parameter(required = true, cache = true)
	public abstract IPropertyDescriptor getDescriptor();

	public abstract void setDescriptor(IPropertyDescriptor descriptor);

	@Parameter(required = true, cache = true)
	public abstract Object getOwner();

	public abstract void setOwner(Object owner);

	@Parameter(required = true, cache = true)
	public abstract Object getAssociation();

	public abstract void setAssociation(Object association);

	@Parameter(required = true, cache = true)
	public abstract Object getValue();

	public abstract void setValue(Object value);

	@Parameter(required = true, cache = true)
	public abstract Object getModel();

	public abstract void setModel(Object bytes);

	@InjectState("callbackStack")
	public abstract CallbackStack getCallbackStack();

	@InjectObject("spring:pageResolver")
	public abstract PageResolver getPageResolver();

	@InjectObject("spring:persistenceService")
	public abstract PersistenceService getPersistenceService();

	@InjectObject("spring:editorService")
	public abstract BlockFinder getBlockFinder();

	@InjectObject("spring:descriptorService")
	public abstract DescriptorService getDescriptorService();

	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(
			getDescriptor().getPropertyType());
	}

	public AssociationMgt()
	{
		super();
	}

	public String getOwnerTypeName()
	{
		return getOwner().getClass().getName();
	}

	public Class getOwnerType()
	{
		return getOwner().getClass();
	}

	public String getAssociationTypeName()
	{
		return getDescriptor().getPropertyType().getCanonicalName();
	}

	public Class getAssociationType()
	{
		return getDescriptor().getPropertyType().getClass();
	}

	AssociationCallback buildCallback()
	{
		AssociationCallback callback = new AssociationCallback(getPage()
			.getRequestCycle().getPage().getPageName(), getModel(),
			getObjectReferenceDescriptor());
		return callback;
	}

	public void addNew(IRequestCycle cycle)
	{
		getCallbackStack().push(buildCallback());

		String currentEditPageName = getPage().getRequestCycle().getPage()
			.getPageName();
		EditPage ownerEditPage = (EditPage) getPageResolver().resolvePage(
			cycle, getDescriptor().getClass().getName(), PageType.EDIT);

		try
		{
			Object newModel = buildNewMemberInstance();
			EditCallback nextPage = new EditCallback(ownerEditPage
				.getPageName(), newModel);

			((EditPage) cycle.getPage(currentEditPageName))
				.setNextPage(nextPage);
		} catch (Exception ex)
		{
			throw new TrailsRuntimeException(ex);
		}
	}

	protected Object buildNewMemberInstance() throws InstantiationException,
		IllegalAccessException
	{
		Object associationModel;
		if (getCreateExpression() == null)
		{
			associationModel = getDescriptor().getPropertyType().newInstance();
		} else
		{
			try
			{
				associationModel = Ognl.getValue(getCreateExpression(),
					getOwner());
			} catch (OgnlException oe)
			{
				oe.printStackTrace();
				return null;
			}
		}

		if (getObjectReferenceDescriptor().getInverseProperty() != null
			&& getObjectReferenceDescriptor().isOneToOne())
		{
			try
			{
				Ognl.setValue(getObjectReferenceDescriptor()
					.getInverseProperty(), associationModel, getOwner());
			} catch (OgnlException e)
			{
				LOG.error(e.getMessage());
			}
		}

		return associationModel;
	}

	public void remove(IRequestCycle cycle)
	{
		EditPage editPage = (EditPage) getPageResolver().resolvePage(cycle,
			getDescriptor().getClass().getName(), PageType.EDIT);

		AssociationCallback callback = buildCallback();

		callback.remove(getPersistenceService(), getAssociation());
		cycle.activate(editPage);

		callback.performCallback(cycle);
	}

	public abstract EditLink getEditLink();

	public abstract Insert getLinkInsert();

	public OwningObjectReferenceDescriptor getObjectReferenceDescriptor()
	{
		return (OwningObjectReferenceDescriptor) getDescriptor();
	}
}
