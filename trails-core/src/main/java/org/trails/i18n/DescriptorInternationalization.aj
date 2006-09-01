/**
  * Created on 25/11/2005 by Eduardo Piva - eduardo@gwe.com.br
  */
package org.trails.i18n;

import java.util.Locale;

import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.servlet.TrailsApplicationServlet;

/**
  * This class doesn't have a Aspect name (Ex.: DescriptorInternationalizationAspect) because we
  * don't want it to be inserted in the trails-aspect.jar
  *
  */
public aspect DescriptorInternationalization {

	private ResourceBundleMessageSource messageSource;
    
    private LocaleHolder localeHolder;
		
	pointcut internationalizeDisplayName(IDescriptor descriptor) : execution(* IDescriptor.getDisplayName())
									&& this(descriptor)
									&& !cflowbelow(execution(* IDescriptor+.*(..)));

	pointcut internationalizePluralDisplayName(IClassDescriptor descriptor) : execution(* IClassDescriptor.getPluralDisplayName())
									&& this(descriptor)
									&& !cflowbelow(execution(* IClassDescriptor+.*(..)));

    private Locale getLocale()
    {
        return getLocaleHolder() == null ? null : getLocaleHolder().getLocale();
    }
    
	Object around(IDescriptor descriptor) : internationalizeDisplayName(descriptor) {
		String displayName = (String) proceed(descriptor);
		return (messageSource != null)
					? messageSource.getDisplayName(descriptor, getLocale(), displayName)
					: displayName;
	}

	Object around(IClassDescriptor classDescriptor) : internationalizePluralDisplayName(classDescriptor) {
		String displayName = (String) proceed(classDescriptor);
		return (messageSource != null)
					? messageSource.getPluralDislayName(classDescriptor, getLocale(), displayName)
					: displayName;
	
	}

	/* setters for ioc attributes */
	public void setResourceBundleMessageSource(ResourceBundleMessageSource source) {
		this.messageSource = source;
	}
	
    public void setLocaleHolder(LocaleHolder localeHolder)
    {
        this.localeHolder = localeHolder;
    }

    public LocaleHolder getLocaleHolder()
    {
        return this.localeHolder;
    }
}
