package org.trails.component;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.callback.CollectionCallback;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.EditPage;
import org.trails.page.TrailsPage.PageType;
import org.trails.persistence.HibernatePersistenceService;


/**
 * This component produces a editor for a ManyToOne or ManyToMany collection
 */
@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class HibernateEditCollection extends EditCollection
{

	@Asset(value = "/org/trails/component/EditCollection.html")
	public abstract IAsset get$template();

	protected static final Log LOG = LogFactory.getLog(HibernateEditCollection.class);

	/**
	 * @todo: remove when the components reuse issue goes away
	 */
	@InjectObject("spring:persistenceService")
	public abstract HibernatePersistenceService getHibernatePersistenceService();

	/**
	 * @todo: remove when the components reuse issue goes away
	 */
	@Override
	public HibernatePersistenceService getPersistenceService()
	{
		return getHibernatePersistenceService();
	}

	public IPage edit(Object member)
	{

		CollectionCallback callback = new CollectionCallback(
			getPage().getRequestCycle().getPage().getPageName(),
			getModel(),
			getCollectionDescriptor());

		getCallbackStack().push(callback);

		EditPage editPage = (EditPage) getPageResolver().resolvePage(
			getPage().getRequestCycle(),
			Utils.checkForCGLIB(member.getClass()),
			PageType.Edit);
		try
		{
			getPersistenceService().reattach(member);
		} catch (NonUniqueObjectException e)
		{
//			member = getPersistenceService().reload(member); //@todo; remove it
		}
		editPage.setModel(member);
		return editPage;
	}

	/**
	 * @return
	 */
	public IPropertySelectionModel getSelectionModel()
	{
		IClassDescriptor elementDescriptor = getDescriptorService().getClassDescriptor(getCollectionDescriptor().getElementType());

		// don't allow use to select from all here
		if (getCollectionDescriptor().isChildRelationship())
		{
			return new IdentifierSelectionModel(getSelectedList(), elementDescriptor.getIdentifierDescriptor().getName());
		}
		// but do here
		else if (getCollectionDescriptor().getInverseProperty() != null && getCollectionDescriptor().isOneToMany())
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(getCollectionDescriptor().getElementType());
			String identifier = elementDescriptor.getIdentifierDescriptor().getName();
			if (getModel() != null)
			{
				try
				{
					criteria.add(
						Restrictions.disjunction()
							.add(Restrictions.isNull(getCollectionDescriptor().getInverseProperty()))
							.add(Restrictions.eq(getCollectionDescriptor().getInverseProperty() + "." + identifier, Ognl.getValue(elementDescriptor.getIdentifierDescriptor().getName(), getModel()))));
				} catch (OgnlException e)
				{
					LOG.error(e.getMessage());
				}
			} else
			{
				criteria.add(Restrictions.isNull(getCollectionDescriptor().getInverseProperty()));
			}

			return new IdentifierSelectionModel(getPersistenceService().getInstances(getCollectionDescriptor().getElementType(), criteria), elementDescriptor.getIdentifierDescriptor().getName());
		} else
		{
			return new IdentifierSelectionModel(getPersistenceService().getAllInstances(getCollectionDescriptor().getElementType()),
				elementDescriptor.getIdentifierDescriptor().getName());
		}
	}
}
