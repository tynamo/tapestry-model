package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.CollectionDescriptor;
import org.tynamo.descriptor.extension.CollectionExtension;

public class CollectionDescriptorAnnotationHandler implements DescriptorAnnotationHandler<CollectionDescriptor, TynamoPropertyDescriptor>
{

	public void decorateFromAnnotation(CollectionDescriptor annotation, TynamoPropertyDescriptor descriptor)
	{
		CollectionExtension extension = new CollectionExtension();
		descriptor.addExtension(CollectionExtension.class, extension);

		extension.setChildRelationship(annotation.child());
		extension.setAllowRemove(annotation.allowRemove());

		if (!CollectionDescriptor.DEFAULT_inverse.equals(annotation.inverse()))
		{
			extension.setInverseProperty(annotation.inverse());
		}

		if (!CollectionDescriptor.DEFAULT_addExpression.equals(annotation.addExpression()))
		{
			extension.setAddExpression(annotation.addExpression());
		}

		if (!CollectionDescriptor.DEFAULT_removeExpression.equals(annotation.removeExpression()))
		{
			extension.setRemoveExpression(annotation.removeExpression());
		}

		if (!CollectionDescriptor.DEFAULT_swapExpression.equals(annotation.swapExpression()))
		{
			extension.setSwapExpression(annotation.swapExpression());
		}

	}
}
