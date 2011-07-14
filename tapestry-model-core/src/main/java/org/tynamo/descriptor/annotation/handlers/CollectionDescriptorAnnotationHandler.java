package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.annotation.CollectionDescriptor;

public class CollectionDescriptorAnnotationHandler implements DescriptorAnnotationHandler<CollectionDescriptor, org.tynamo.descriptor.CollectionDescriptor>
{

	public void decorateFromAnnotation(CollectionDescriptor annotation, org.tynamo.descriptor.CollectionDescriptor descriptor)
	{
		descriptor.setChildRelationship(annotation.child());
		descriptor.setAllowRemove(annotation.allowRemove());

		if (!CollectionDescriptor.DEFAULT_inverse.equals(annotation.inverse()))
		{
			descriptor.setInverseProperty(annotation.inverse());
		}

		if (!CollectionDescriptor.DEFAULT_addExpression.equals(annotation.addExpression()))
		{
			descriptor.setAddExpression(annotation.addExpression());
		}

		if (!CollectionDescriptor.DEFAULT_removeExpression.equals(annotation.removeExpression()))
		{
			descriptor.setRemoveExpression(annotation.removeExpression());
		}

		if (!CollectionDescriptor.DEFAULT_swapExpression.equals(annotation.swapExpression()))
		{
			descriptor.setSwapExpression(annotation.swapExpression());
		}

	}
}
