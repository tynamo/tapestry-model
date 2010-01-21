package org.tynamo.model.jpa.services;

import javax.persistence.Parameter;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.metamodel.Type;

public class QueryParameter
{
	private String name;
	private Object value;
	private Type type;

	/**
	 * @param name  the name of the parameter
	 * @param value the value of the parameter
	 */
	public QueryParameter(String name, Object value)
	{
		this(name, value, null);
	}

	/**
	 * @param name  the name of the parameter
	 * @param value the value of the parameter
	 * @param type  Hibernate type of the parameter (or <code>null</code> if
	 *              none specified)
	 */
	public QueryParameter(String name, Object value, Type type)
	{
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public Object getValue()
	{
		return value;
	}

	public QueryParameter(Type type)
	{
		this.type = type;
	}

	public Type getType()
	{
		return type;
	}


	/**
	 * Apply the parameters to the given Query object.
	 *
	 * @param queryObject the Query object
	 *          if thrown by the Query object
	 */
	public void applyNamedParameterToQuery(Query queryObject) throws PersistenceException
	{
		/* TODO
		if (value instanceof Collection)

		{
			if (type != null)
			{
				queryObject.setParameterList(name, (Collection) value, type);
			} else
			{
				queryObject.setParameterList(name, (Collection) value);
			}
		} else if (value instanceof Object[])
		{
			if (type != null)
			{
				queryObject.setParameterList(name, (Object[]) value, type);
			} else
			{
				queryObject.setParameterList(name, (Object[]) value);
			}
		} else
		{
		 */
			if (type != null)
			{
				queryObject.setParameter(new Parameter() {
					public String getName() {
						return name;
					}

					public Integer getPosition() {
						return null;
					}

					public Class getParameterType() {
						return null;  //To change body of implemented methods use File | Settings | File Templates.
					}
				},type);
			} else
			{
				queryObject.setParameter(name, value);
			}
		//}
	}

}

