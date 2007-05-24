package org.trails.descriptor.annotation;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IDescriptorExtension;
import org.trails.descriptor.IPropertyDescriptor;

public class CollectionDescriptorAnnotationHandler extends
		AbstractAnnotationHandler implements
		DescriptorAnnotationHandler<Collection, IPropertyDescriptor> {

	public CollectionDescriptorAnnotationHandler() {
		super();
	}

	public IPropertyDescriptor decorateFromAnnotation(Collection annotation,
			IPropertyDescriptor descriptor) {
		/**
		 * Retrieve elementType from extensions?... 
		 * PROBLEM... if they have no extensions, we get no elementType !!!
		 * 
		 * This was suppose to be on the fly in play... expected to at least
		 * have one. Maybe the test case did not set up correctly... YES.
		 */
		Class elementType = null;
		Map<String, IDescriptorExtension> extensions = descriptor.getExtensions();
		Set set = extensions.entrySet();
		Iterator iter = set.iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			IDescriptorExtension ext = (IDescriptorExtension) entry
					.getValue(); // classes annotations
			elementType = ((CollectionDescriptor) ext).getElementType();
			break;
		}

		CollectionDescriptor collectionDescriptor = new CollectionDescriptor(
				descriptor.getPropertyType(), descriptor, elementType);
		setPropertiesFromAnnotation(annotation, collectionDescriptor);

		collectionDescriptor.setChildRelationship(annotation.child());
		if (!annotation.DEFAULT_inverse.equals(annotation.inverse())) {
			collectionDescriptor.setInverseProperty(annotation.inverse());
		}

		descriptor.addExtension(collectionDescriptor.getClass().getName(),
				collectionDescriptor);
		return descriptor;
	}
}