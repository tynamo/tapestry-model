/*
 * Copyright 2004 Chris Nelson
 *
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
package org.trails.hibernate;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;


/**
 * <b>Created:<b> Apr 27, 2004<br><br>
 * <p/>
 * <b>Description:</b><br>
 * Class required by Hibernate when you have an object with composite primary key.
 * The isUnsaved() method is what we're interested in here.
 * <p/>
 * <br>
 * <d>Revision History:</b><br>
 * ----------------------------------------------------------------------------------<br>
 * Version            Date            Author        Comments<br>
 * ----------------------------------------------------------------------------------<br>
 * 1.0                Apr 27, 2004    CRD3036        Initial Version.
 * <br> <br>
 *
 * @author CRD3036
 * @version 1.0
 */
public class TrailsInterceptor implements Interceptor, Serializable
{
	/**
	 *
	 */
	public TrailsInterceptor()
	{
		super();
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#onLoad(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
		 */
	public boolean onLoad(Object entity, Serializable id, Object[] state,
						  String[] propertyNames, Type[] types)
	{
		if (entity instanceof Interceptable)
		{
			return ((Interceptable) entity).onLoad(id, state, propertyNames, types);
		}

		return false;
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
		 */
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
								Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException
	{
		if (entity instanceof Interceptable)
		{
			return ((Interceptable) entity).onUpdate(id, currentState, previousState, propertyNames, types);
		}
		return false;
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
		 */
	public boolean onSave(Object entity, Serializable id, Object[] state,
						  String[] propertyNames, Type[] types) throws CallbackException
	{
		if (entity instanceof Interceptable)
		{
			return ((Interceptable) entity).onInsert(id, state, propertyNames, types);
		}

		return false;
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#onDelete(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
		 */
	public void onDelete(Object entity, Serializable id, Object[] state,
						 String[] propertyNames, Type[] types) throws CallbackException
	{
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#preFlush(java.util.Iterator)
		 */
	public void preFlush(Iterator arg0) throws CallbackException
	{
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#postFlush(java.util.Iterator)
		 */
	public void postFlush(Iterator arg0) throws CallbackException
	{
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#isUnsaved(java.lang.Object)
		 */
	public Boolean isUnsaved(Object arg0)
	{
		if (arg0 instanceof Interceptable)
		{
			return new Boolean((!((Interceptable) arg0).isSaved()));
		} else
		{
			return null;
		}
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#findDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
		 */
	public int[] findDirty(Object arg0, Serializable arg1, Object[] arg2,
						   Object[] arg3, String[] arg4, Type[] arg5)
	{
		return null;
	}

	/* (non-Javadoc)
		 * @see org.hibernate.Interceptor#instantiate(java.lang.Class, java.io.Serializable)
		 */
	public Object instantiate(Class arg0, Serializable arg1)
		throws CallbackException
	{
		return null;
	}

	public Boolean isTransient(Object arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object instantiate(String arg0, EntityMode arg1, Serializable arg2) throws CallbackException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getEntityName(Object arg0) throws CallbackException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getEntity(String arg0, Serializable arg1) throws CallbackException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void afterTransactionBegin(Transaction arg0)
	{
		// TODO Auto-generated method stub

	}

	public void beforeTransactionCompletion(Transaction arg0)
	{
		// TODO Auto-generated method stub

	}

	public void afterTransactionCompletion(Transaction arg0)
	{
		// TODO Auto-generated method stub

	}

	public void onCollectionRecreate(Object arg0, Serializable arg1) throws CallbackException
	{
		// TODO Auto-generated method stub

	}

	public void onCollectionRemove(Object arg0, Serializable arg1) throws CallbackException
	{
		// TODO Auto-generated method stub

	}

	public void onCollectionUpdate(Object arg0, Serializable arg1) throws CallbackException
	{
		// TODO Auto-generated method stub

	}

	public String onPrepareStatement(String statement)
	{
		return statement;
	}
}
