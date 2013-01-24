package org.tynamo.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.type.Type;

import java.util.Collection;

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
	 * @throws org.hibernate.HibernateException
	 *          if thrown by the Query object
	 */
	public void applyNamedParameterToQuery(Query queryObject) throws HibernateException
	{
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
			if (type != null)
			{
				queryObject.setParameter(name, value, type);
			} else
			{
				queryObject.setParameter(name, value);
			}
		}
	}

}

