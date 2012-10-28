package org.tynamo.hibernate;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.hibernate.search.annotations.Indexed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.decorators.DescriptorDecorator;

public class HibernateSearchDescriptorDecorator implements DescriptorDecorator {
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSearchDescriptorDecorator.class);

	@Override
	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor) {
		Class type = descriptor.getBeanType();

		if (!type.isAnnotationPresent(Indexed.class)) return descriptor;
		descriptor.setSearchable(true);

		PropertyDescriptor[] propertyDescriptors;
		try {
			propertyDescriptors = Introspector.getBeanInfo(type).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			LOGGER.warn(ExceptionUtils.getRootCauseMessage(e));
			return descriptor;
		}

		// we are using Hibernate Search annotations so we cannot use any of our own nice annotation handling machinery
		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
			try {
				Field propertyField = propertyDescriptor.getBeanType().getDeclaredField(propertyDescriptor.getName());
				if (propertyField.isAnnotationPresent(org.hibernate.search.annotations.Field.class)) {
					propertyDescriptor.setSearchable(true);
					continue;
				}
			} catch (Exception ex) {
				LOGGER.warn(ExceptionUtils.getRootCauseMessage(ex));
			}
			try {
				if (isAnnotationPresent(propertyDescriptors, propertyDescriptor, org.hibernate.search.annotations.Field.class)) {
					propertyDescriptor.setSearchable(true);
					continue;
				}
			} catch (Exception ex) {
				LOGGER.warn(ExceptionUtils.getRootCauseMessage(ex));
			}
		}
		return descriptor;
	}

	private boolean isAnnotationPresent(PropertyDescriptor[] descriptors,
		final TynamoPropertyDescriptor tynamoPropertyDescriptor,
		Class<? extends Annotation> annotation) {
		// copied from TynamoDecorator. Perhaps the right thing to do would be to push to implement PropertyAccess.getAnnotation()?
		try {

			PropertyDescriptor beanPropDescriptor = F.flow(descriptors).filter(new Predicate<PropertyDescriptor>() {
				public boolean accept(PropertyDescriptor descriptor) {
					return descriptor.getName().equals(tynamoPropertyDescriptor.getName());
				}
			}).first();
			if (beanPropDescriptor == null) return false;

			Method readMethod = beanPropDescriptor.getReadMethod();
			return readMethod.isAnnotationPresent(annotation);

		} catch (Exception ex) {
			LOGGER.warn(ExceptionUtils.getRootCauseMessage(ex));
		}
		return false;
	}

}
