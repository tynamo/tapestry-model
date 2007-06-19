package org.trails.callback;

import ognl.Ognl;
import ognl.OgnlException;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.ObjectReferenceDescriptor;
import org.trails.descriptor.OwningObjectReferenceDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * This guy is responsible for returning from an add or remove on a association.
 */
public class AssociationCallback extends EditCallback
{
	private ObjectReferenceDescriptor od;

	private boolean oneToOne;

	/**
	 * @param pageName
	 * @param model
	 */
	public AssociationCallback(String pageName, Object model, ObjectReferenceDescriptor od)
	{
		super(pageName, model);
		this.od = od;
		this.setOneToOne(od.supportsExtension(OwningObjectReferenceDescriptor.class.getName()));
	}

	public void save(PersistenceService persistenceService, Object newObject)
	{
		executeOgnlExpression(findAddExpression(), newObject);
		persistenceService.save(getModel());
	}

	public void remove(PersistenceService persistenceService, Object object)
	{
		try
		{
			Ognl.setValue(findAddExpression(), model, null);
			Ognl.getValue(findAddExpression(), model);

			persistenceService.save(getModel());
			persistenceService.remove(object);
		} catch (OgnlException e)
		{
			throw new TrailsRuntimeException(e, getModel().getClass());
		}
	}

	/**
	 * @param previousModel
	 */
	private void executeOgnlExpression(String ognlExpression, Object newObject)
	{

		try
		{
			Ognl.setValue(ognlExpression, model, newObject);
			Ognl.getValue(ognlExpression, model);
		} catch (OgnlException e)
		{
			throw new TrailsRuntimeException(e, model.getClass());
		}
	}

	public boolean isOneToOne()
	{
		return oneToOne;
	}

	public void setOneToOne(boolean oneToOne)
	{
		this.oneToOne = oneToOne;
	}

	private String findAddExpression()
	{
		return od.getName();
	}
}
