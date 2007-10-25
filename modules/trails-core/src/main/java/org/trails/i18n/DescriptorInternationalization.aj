/**
  * Created on 25/11/2005 by Eduardo Piva - eduardo@gwe.com.br
  */
package org.trails.i18n;

import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.servlet.TrailsApplicationServlet;

/**
  * This class doesn't have a Aspect name (Ex.: DescriptorInternationalizationAspect) because we
  * don't want it to be inserted in the trails-aspect.jar
  *
  */
public aspect DescriptorInternationalization {

	private TrailsMessageSource messageSource;

	pointcut internationalizeDisplayName(IDescriptor descriptor) : execution(* IDescriptor.getDisplayName())
									&& this(descriptor)
									&& !cflowbelow(execution(* IDescriptor+.*(..)));

	pointcut internationalizePluralDisplayName(IClassDescriptor descriptor) : execution(* IClassDescriptor.getPluralDisplayName())
									&& this(descriptor)
									&& !cflowbelow(execution(* IClassDescriptor+.*(..)));

	Object around(IDescriptor descriptor) : internationalizeDisplayName(descriptor) {
		String displayName = (String) proceed(descriptor);
		return (messageSource != null)
					? messageSource.getDisplayName(descriptor, displayName)
					: displayName;
	}

	Object around(IClassDescriptor classDescriptor) : internationalizePluralDisplayName(classDescriptor) {
		String displayName = (String) proceed(classDescriptor);
		return (messageSource != null)
					? messageSource.getPluralDislayName(classDescriptor, displayName)
					: displayName;
	
	}

	/* setters for ioc attributes */
	public void setTrailsMessageSource(TrailsMessageSource source) {
		this.messageSource = source;
	}
}
