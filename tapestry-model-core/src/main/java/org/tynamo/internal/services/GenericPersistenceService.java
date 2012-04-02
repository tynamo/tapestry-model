package org.tynamo.internal.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.services.PersistenceService;

public abstract class GenericPersistenceService implements PersistenceService {
	
	private PropertyAccess propertyAccess;

	public GenericPersistenceService(PropertyAccess propertyAccess) {
		this.propertyAccess = propertyAccess;
	}
	
	protected final PropertyAccess getPropertyAccess() {
		return propertyAccess;
	}
	
	@Override
	public <T> T addToCollection(CollectionDescriptor descriptor, T element, Object collectionOwner) {
		Class elementType = descriptor.getElementType();
		String addMethod = descriptor.getAddExpression() != null ? descriptor.getAddExpression() : "add" + elementType.getSimpleName();

		try
		{
			Method method = descriptor.getBeanType().getMethod(addMethod, new Class[]{elementType});
			method.invoke(collectionOwner, element);
			return element;

		} catch (NoSuchMethodException e)
		{
			// do nothing;
		} catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		} catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		Collection collection = (Collection) propertyAccess.get(collectionOwner, descriptor.getName());
		if (!(descriptor.isChildRelationship() && (collection instanceof List) && (collection.contains(element))))
		{
			collection.add(element);
		}
		return element;
	}
	
	public void removeFromCollection(CollectionDescriptor descriptor, Object element, Object collectionOwner)
	{
		Class elementType = descriptor.getElementType();
		String removeMethod = descriptor.getRemoveExpression() != null ? descriptor.getRemoveExpression() : "remove" + elementType.getSimpleName();

		try
		{
			Method method = descriptor.getBeanType().getMethod(removeMethod, new Class[]{elementType});
			method.invoke(collectionOwner, element);
			return;

		} catch (NoSuchMethodException e)
		{
			// do nothing;
		} catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		} catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		Collection collection = (Collection) getPropertyAccess().get(collectionOwner, descriptor.getName());
		collection.remove(element);
	}
	

}
