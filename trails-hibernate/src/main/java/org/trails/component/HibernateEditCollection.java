package org.trails.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.descriptor.IClassDescriptor;
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
	@InjectObject("service:trails.hibernate.PersistenceService")
	public abstract HibernatePersistenceService getHibernatePersistenceService();

	/**
	 * @todo: remove when the components reuse issue goes away
	 */
	@Override
	public HibernatePersistenceService getPersistenceService()
	{
		return getHibernatePersistenceService();
	}

	@Parameter(required = false)
	public abstract DetachedCriteria getCriteria();

	/**
	 * @return
	 */
	public IPropertySelectionModel getSelectionModel()
	{
		IClassDescriptor elementDescriptor = getDescriptorService().getClassDescriptor(getCollectionDescriptor().getElementType());

		if (getCriteria() == null)
		{
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
					criteria.add(
							Restrictions.disjunction()
									.add(Restrictions.isNull(getCollectionDescriptor().getInverseProperty()))
									.add(Restrictions.eq(
									getCollectionDescriptor().getInverseProperty() + "." + identifier,
									getPersistenceService().getIdentifier(getModel(), elementDescriptor))));
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
		} else
		{
			return new IdentifierSelectionModel(
					getPersistenceService().getInstances(getCollectionDescriptor().getElementType(), getCriteria()),
					elementDescriptor.getIdentifierDescriptor().getName());
		}
	}
}
