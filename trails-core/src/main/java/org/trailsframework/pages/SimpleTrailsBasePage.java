package org.trails.page;

import org.apache.tapestry.annotations.InjectObject;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.i18n.TrailsMessageSource;
import org.trails.persistence.PersistenceService;


public interface SimpleTrailsBasePage
{

	IClassDescriptor getClassDescriptor();

	void setClassDescriptor(IClassDescriptor iClassDescriptor);

	/**
	 * This property is injected with the persistenceService bean
	 *
	 * @return
	 */
	@InjectObject("service:trails.core.PersistenceService")
	PersistenceService getPersistenceService();

	/**
	 * This property is injected with the descriptorService bean
	 *
	 * @return
	 */
	@InjectObject("service:trails.core.DescriptorService")
	DescriptorService getDescriptorService();

	/**
	 * Message source to i18n pages
	 *
	 * @return
	 */
	@InjectObject("service:trails.core.MessageSource")
	TrailsMessageSource getResourceBundleMessageSource();
}
