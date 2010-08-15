package org.tynamo.descriptor.factories;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptorImpl;
import org.tynamo.descriptor.decorators.DescriptorDecorator;

import java.beans.BeanInfo;
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

	protected static final Log LOG = LogFactory.getLog(ReflectionDescriptorFactory.class);

	private final MethodDescriptorFactory methodDescriptorFactory;
	private final PropertyDescriptorFactory propertyDescriptorFactory;
	private final List<DescriptorDecorator> decorators;

	/**
	 * @param decorators				In the default Tynamo configuration this will contain a HibernateDescriptorDecorator and an AnnotationDecorator
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
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e)
		{
			LOG.error(e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Have the decorators decorate this descriptor
	 *
	 * @param descriptor
	 * @param decorators
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
