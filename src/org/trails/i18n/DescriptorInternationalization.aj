/**
  * Created on 25/11/2005 by Eduardo Piva - eduardo@gwe.com.br
  */
package org.trails.i18n;

import java.util.Locale;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.i18n.ResourceBundleMessageSource;
import org.trails.servlet.TrailsApplicationServlet;
import org.apache.hivemind.Registry;
import org.apache.tapestry.services.RequestLocaleManager;

/**
  * This class doesn't have a Aspect name (Ex.: DescriptorInternationalizationAspect) because we
  * don't want it to be inserted in the trails-aspect.jar
  *
  */
public aspect DescriptorInternationalization {

	private ResourceBundleMessageSource messageSource;
		
	pointcut internationalizeDisplayName(IDescriptor descriptor) : execution(* IDescriptor.getDisplayName())
									&& this(descriptor)
									&& !cflowbelow(execution(* IDescriptor+.*(..)));

	pointcut internationalizePluralDisplayName(IClassDescriptor descriptor) : execution(* IClassDescriptor.getPluralDisplayName())
									&& this(descriptor)
									&& !cflowbelow(execution(* IClassDescriptor+.*(..)));

	Object around(IDescriptor descriptor) : internationalizeDisplayName(descriptor) {
		Locale locale = TrailsApplicationServlet.getCurrentLocale();

		String displayName = (String) proceed(descriptor);
		return (messageSource != null)
					? messageSource.getDisplayName(descriptor, locale, displayName)
					: displayName;
	}

	Object around(IClassDescriptor classDescriptor) : internationalizePluralDisplayName(classDescriptor) {
		Locale locale = TrailsApplicationServlet.getCurrentLocale();
		
		String displayName = (String) proceed(classDescriptor);
		return (messageSource != null)
					? messageSource.getPluralDislayName(classDescriptor, locale, displayName)
					: displayName;
	
	}

	/* setters for ioc attributes */
	public void setResourceBundleMessageSource(ResourceBundleMessageSource source) {
		this.messageSource = source;
	}
	

}
