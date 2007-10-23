package org.trails.component;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Lifecycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.callback.ICallback;
import org.trails.TrailsRuntimeException;
import org.trails.callback.AssociationCallback;
import org.trails.callback.CallbackStack;
import org.trails.callback.EditCallback;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.ObjectReferenceDescriptor;
import org.trails.descriptor.OwningObjectReferenceDescriptor;
import org.trails.page.EditPage;
import org.trails.page.PageResolver;
import org.trails.page.PageType;
import org.trails.persistence.PersistenceException;
import org.trails.persistence.PersistenceService;
import org.trails.validation.TrailsValidationDelegate;

/**
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 * @OneToOne use case. <p/> This guy manages the owning side user interface of a
 *           OneToOne association. <p/> Owner-<>-----Association
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
	public abstract ObjectReferenceDescriptor getDescriptor();

	public abstract void setDescriptor(ObjectReferenceDescriptor descriptor);

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

	public abstract void setCallbackStack(CallbackStack stack);

	@InjectObject("service:trails.core.PageResolver")
	public abstract PageResolver getPageResolver();

	@InjectObject("service:trails.core.PersistenceService")
	public abstract PersistenceService getPersistenceService();

	@InjectObject("service:trails.core.EditorService")
	public abstract BlockFinder getBlockFinder();

	@InjectObject("service:trails.core.DescriptorService")
	public abstract DescriptorService getDescriptorService();

	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(getDescriptor().getPropertyType());
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
		AssociationCallback callback = new AssociationCallback(getPage().getRequestCycle().getPage().getPageName(),
				getModel(), getDescriptor());
		return callback;
	}

	public void addNew(IRequestCycle cycle)
	{
		getCallbackStack().push(buildCallback());

		String currentEditPageName = getPage().getRequestCycle().getPage().getPageName();
		EditPage ownerEditPage = (EditPage) getPageResolver().resolvePage(cycle, getDescriptor().getClass(),
				PageType.Edit);

		try
		{
			Object newModel = buildNewMemberInstance();
			EditCallback nextPage = new EditCallback(ownerEditPage.getPageName(), newModel);

			((EditPage) cycle.getPage(currentEditPageName)).setNextPage(nextPage);
			nextPage.performCallback(cycle);
		} catch (Exception ex)
		{
			throw new TrailsRuntimeException(ex, getDescriptor().getClass().getClass());
		}
	}

	public void remove(IRequestCycle cycle)
	{
		/**
		 * This is a direct hit on the same page. No need to setup callbackstack.
		 */
		EditPage editPage = (EditPage) getPageResolver().resolvePage(cycle, getDescriptor().getClass(), PageType.Edit);

		AssociationCallback callback = buildCallback();

		callback.remove(getPersistenceService(), getAssociation());
		cycle.activate(editPage);

		EditCallback nextPage = new EditCallback(editPage.getPageName(), getModel());

		String currentEditPageName = getPage().getRequestCycle().getPage().getPageName();
		((EditPage) cycle.getPage(currentEditPageName)).setNextPage(nextPage);
		nextPage.performCallback(cycle);
	}

	public IPage edit(Object member)
	{
		AssociationCallback callback = new AssociationCallback(getPage().getRequestCycle().getPage().getPageName(),
				getModel(), getDescriptor());
		getCallbackStack().push(callback);
		EditPage editPage = (EditPage) getPageResolver().resolvePage(getPage().getRequestCycle(),
				Utils.checkForCGLIB(member.getClass()), PageType.Edit);

		editPage.setModel(member);
		return editPage;
	}

	protected Object buildNewMemberInstance() throws InstantiationException, IllegalAccessException
	{
		Object associationModel;
		if (getCreateExpression() == null)
		{
			associationModel = getDescriptor().getPropertyType().newInstance();
		} else
		{
			try
			{
				associationModel = Ognl.getValue(getCreateExpression(), getOwner());
			} catch (OgnlException oe)
			{
				oe.printStackTrace();
				return null;
			}
		}

		if (getOwningObjectReferenceDescriptor() != null
				&& getOwningObjectReferenceDescriptor().getInverseProperty() != null)
		{
			try
			{
				Ognl.setValue(getOwningObjectReferenceDescriptor().getInverseProperty(), associationModel, getOwner());
			} catch (OgnlException e)
			{
				LOG.error(e.getMessage());
			}
		}

		return associationModel;
	}

	public OwningObjectReferenceDescriptor getOwningObjectReferenceDescriptor()
	{
		return getDescriptor().getExtension(OwningObjectReferenceDescriptor.class);
	}
}
