package org.tynamo.descriptor.factories;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptorImpl;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.exception.TynamoRuntimeException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Generate descriptors using reflection on the underlying class.
 * ReflectionDescriptorFactory.buildClassDescriptor() is the only public method
 * here.
 */
public class ReflectionDescriptorFactory implements DescriptorFactory
{

	private static final Logger logger = LoggerFactory.getLogger(ReflectionDescriptorFactory.class);

	private final MethodDescriptorFactory methodDescriptorFactory;
	private final PropertyDescriptorFactory propertyDescriptorFactory;
	private final List<DescriptorDecorator> decorators;

	/**
	 * @param decorators				In the default Tynamo configuration this will contain a HibernateDescriptorDecorator and an TynamoDecorator
	 * @param methodDescriptorFactory
	 * @param propertyDescriptorFactory
	 */
	public ReflectionDescriptorFactory(final List<DescriptorDecorator> decorators, MethodDescriptorFactory methodDescriptorFactory, PropertyDescriptorFactory propertyDescriptorFactory)
	{
		this.decorators = decorators;
		this.methodDescriptorFactory = methodDescriptorFactory;
		this.propertyDescriptorFactory = propertyDescriptorFactory;
	}

	/**
	 * Given a type, build a class descriptor
	 *
	 * @param type The type to build for
	 * @return a completed class descriptor
	 */
	public TynamoClassDescriptor buildClassDescriptor(Class type)
	{
		try
		{
			TynamoClassDescriptor descriptor = new TynamoClassDescriptorImpl(type);
			BeanInfo beanInfo = Introspector.getBeanInfo(type);

			BeanUtils.copyProperties(descriptor, beanInfo.getBeanDescriptor());
			descriptor.setPropertyDescriptors(propertyDescriptorFactory.buildPropertyDescriptors(type, beanInfo));
			descriptor.setMethodDescriptors(methodDescriptorFactory.buildMethodDescriptors(type, beanInfo));

			descriptor = applyDecorators(descriptor);

			return descriptor;

		} catch (IllegalAccessException e)
		{
			logger.error("couldn't build class descriptor for: " + type.getSimpleName(), e);
			throw new TynamoRuntimeException(e, type);
		} catch (InvocationTargetException e)
		{
			logger.error("couldn't build class descriptor for: " + type.getSimpleName(), e);
			throw new TynamoRuntimeException(e, type);
		} catch (IntrospectionException e)
		{
			logger.error("couldn't build class descriptor for: " + type.getSimpleName(), e);
			throw new TynamoRuntimeException(e, type);
		}
	}

	/**
	 * Have the decorators decorate this descriptor
	 *
	 * @param descriptor
	 * @return The resulting descriptor after all decorators are applied
	 */
	private TynamoClassDescriptor applyDecorators(final TynamoClassDescriptor descriptor)
	{
		TynamoClassDescriptor decoratedDescriptor = descriptor;

		for (DescriptorDecorator decorator : decorators)
		{
			decoratedDescriptor = decorator.decorate(decoratedDescriptor);
		}

		return decoratedDescriptor;
	}
}
