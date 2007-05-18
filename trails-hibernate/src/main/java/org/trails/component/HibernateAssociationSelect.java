package org.trails.component;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.page.HibernateEditPage;
import org.trails.page.PageResolver;
import org.trails.persistence.HibernatePersistenceService;

/**
 * @author Chris Nelson
 *         <p/>
 *         This guy interacts with persistence service to produce a Select containing
 *         all the elements of the PropertyDescriptor's type. If a criteria is
 *         specified, it will filter the list by it.
 *         <p/>
 *         Additionally, a detached criteria is available to automatically filter out
 *         associations without owners, or get all owners by default.
 */
public abstract class HibernateAssociationSelect extends BaseComponent
{
	@InjectObject("spring:persistenceService")
	public abstract HibernatePersistenceService getPersistenceService();

	@InjectObject("spring:descriptorService")
	public abstract DescriptorService getDescriptorService();

	@InjectObject("spring:pageResolver")
	public abstract PageResolver getPageResolver();

	public abstract IPropertySelectionModel getPropertySelectionModel();

	public abstract void setPropertySelectionModel(
		IPropertySelectionModel PropertySelectionModel);

	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);

	@Parameter(required = true, cache = true)
	public abstract Object getModel();

	public abstract void setModel(Object bytes);

	@Persist
	public abstract Object getValue();

	public abstract void setValue(Object value);

	@Parameter(required = true, cache = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor propertyDescriptor);

	@Parameter(required = true, cache = true)
	public abstract Object getOwner();

	public abstract void setOwner(Object owner);

	@Parameter(required = true, cache = true)
	public abstract Object getAssociation();

	public abstract void setAssociation(Object association);

	public abstract boolean isAllowNone();

	public abstract void setAllowNone(boolean allowNone);

	public abstract String getNoneLabel();

	public abstract void setNoneLabel(String noneLabel);

	public HibernateAssociationSelect()
	{
		super();
	}

	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(
			getPropertyDescriptor().getPropertyType());
	}

	@Override
	protected void prepareForRender(IRequestCycle cycle)
	{
		buildSelectionModel();

		super.prepareForRender(cycle);
	}

	public void buildSelectionModel()
	{
		DetachedCriteria criteria = getCriteria() != null ? getCriteria()
			: getAllAssociations();
		IdentifierSelectionModel selectionModel = new IdentifierSelectionModel(
			getPersistenceService().getInstances(criteria),
			getClassDescriptor().getIdentifierDescriptor().getName(),
			isAllowNone());
		selectionModel.setNoneLabel(getNoneLabel());
		setPropertySelectionModel(selectionModel);
	}

	public DetachedCriteria getAssociationsWithoutOwners()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(getClassDescriptor().getType());
		try
		{
			HibernateEditPage page = (HibernateEditPage) getPage().getRequestCycle().getPage();
			String ownerOgnlIdentifier = page.getClassDescriptor().getIdentifierDescriptor().getName();
			String associationOgnlIdentifier = getClassDescriptor().getIdentifierDescriptor().getName();
			String inverseOgnlProperty = getPropertyDescriptor().getName(); // league

			if (getOwner() != null)
			{

				criteria.add(Restrictions.disjunction().add(
					Restrictions.isNull(inverseOgnlProperty)).add(
					Restrictions.eq(inverseOgnlProperty + "."
						+ associationOgnlIdentifier, Ognl.getValue(
						ownerOgnlIdentifier, getOwner()))));

			} else
			{
				criteria.add(Restrictions.isNull(inverseOgnlProperty));
			}
		} catch (OgnlException e)
		{
			e.printStackTrace();
		}
		return criteria;
	}

	public DetachedCriteria getAllAssociations()
	{
		DetachedCriteria criteria = DetachedCriteria
			.forClass(getClassDescriptor().getType());
		return criteria;
	}
}