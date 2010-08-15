package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.annotation.Collection;

public class CollectionDescriptorAnnotationHandler implements DescriptorAnnotationHandler<Collection, CollectionDescriptor>
{

	public CollectionDescriptor decorateFromAnnotation(Collection annotation, CollectionDescriptor descriptor)
	{
		descriptor.setChildRelationship(annotation.child());
		descriptor.setAllowRemove(annotation.allowRemove());

		if (!Collection.DEFAULT_inverse.equals(annotation.inverse()))
		{
			descriptor.setInverseProperty(annotation.inverse());
		}

		if (!Collection.DEFAULT_addExpression.equals(annotation.addExpression()))
		{
			descriptor.setAddExpression(annotation.addExpression());
		}

		if (!Collection.DEFAULT_removeExpression.equals(annotation.removeExpression()))
		{
			descriptor.setRemoveExpression(annotation.removeExpression());
		}

		if (!Collection.DEFAULT_swapExpression.equals(annotation.swapExpression()))
		{
			descriptor.setSwapExpression(annotation.swapExpression());
		}


		return descriptor;
	}
}
