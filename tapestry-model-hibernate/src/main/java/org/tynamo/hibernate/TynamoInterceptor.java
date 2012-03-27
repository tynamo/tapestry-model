/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
/*
 * Created on Apr 27, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.tynamo.hibernate;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

import java.io.Serializable;


/**
 * Class required by Hibernate when you have an object with composite primary key. The isUnsaved() method is what we're
 * interested in here.
 */
public class TynamoInterceptor extends EmptyInterceptor implements Interceptor, Serializable
{

	/**
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.Interceptor#onLoad(java.lang.Object, java.io.Serializable, java.lang.Object[],
	 *	  java.lang.String[], org.hibernate.type.Type[])
	 */
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
	{
		return entity instanceof Interceptable && ((Interceptable) entity).onLoad(id, state, propertyNames, types);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[],
	 *	  java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
								Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException
	{
		return entity instanceof Interceptable && ((Interceptable) entity).onUpdate(id, currentState, previousState, propertyNames, types);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.Interceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[],
	 *	  java.lang.String[], org.hibernate.type.Type[])
	 */
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException
	{
		return entity instanceof Interceptable && ((Interceptable) entity).onInsert(id, state, propertyNames, types);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.Interceptor#isTransient(java.lang.Object)
	 */
	public Boolean isTransient(Object arg0)
	{
		if (arg0 instanceof HasAssignedIdentifier)
		{
			return !((HasAssignedIdentifier) arg0).isSaved();
		} else
		{
			return null;
		}
	}
}
