package org.tynamo.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.session.HttpSessionApplicationEvent;
import org.acegisecurity.ui.session.HttpSessionCreatedEvent;
import org.acegisecurity.ui.session.HttpSessionDestroyedEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.tynamo.descriptor.IClassDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;

@Aspect
public class DescriptorSecurity implements ApplicationListener
{
	private static final Log log = LogFactory.getLog(DescriptorSecurity.class);
	// Only used to log a warning each time an element is added to cache when no session events have been published
	private static boolean sessionCreationDetected = false;

	private SecurityService securityService;
	Map<String, Map<String,IClassDescriptor>> perUserClassDescriptorCache = new HashMap<String, Map<String,IClassDescriptor>>();

	@Around(
		"execution(public org.tynamo.descriptor.IClassDescriptor org.tynamo.descriptor.DescriptorService+.getClassDescriptor(Class))")
	public Object classDescriptorSecurity(ProceedingJoinPoint pjp) throws Throwable
	{
		IClassDescriptor desc = (IClassDescriptor) pjp.proceed();
		if (desc != null) {
			SecurityContext context = SecurityContextHolder.getContext();
			if (context == null || context.getAuthentication() == null) return desc;
			return applyRestrictions(desc, context);
		} 
		else return null;
	}

	@Around("execution(public java.util.List org.tynamo.descriptor.DescriptorService+.getAllDescriptors())")
	public Object getAllClassDescriptorSecurity(ProceedingJoinPoint pjp) throws Throwable
	{
		List<IClassDescriptor> descriptors = (List<IClassDescriptor>) pjp.proceed();
		if (descriptors == null) return null;

		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null || context.getAuthentication() == null) return descriptors;

		List<IClassDescriptor> cloneDescriptors = new ArrayList<IClassDescriptor>(descriptors.size());
		for (IClassDescriptor descriptor : descriptors) cloneDescriptors.add(applyRestrictions(descriptor, context) );
		return cloneDescriptors;
	}

	protected IClassDescriptor applyRestrictions(IClassDescriptor descriptor, SecurityContext context)
	{
		// Return a clone specific to this user rather than modify the shared original
		// See if the class descriptor for this user is already cached, otherwise cache
		Map<String,IClassDescriptor> cachedDescriptors = perUserClassDescriptorCache.get(context.getAuthentication().getName());
		IClassDescriptor cloneDescriptor = null;
		if (cachedDescriptors != null) cloneDescriptor = cachedDescriptors.get(descriptor.getType().getSimpleName());
		else {
			cachedDescriptors = new HashMap<String,IClassDescriptor>();
			perUserClassDescriptorCache.put(context.getAuthentication().getName(), cachedDescriptors);
			if (!sessionCreationDetected) log.warn("This implementation caches security-enhanced class descriptors for each user\n "
					+ "but no session events are detected. Descriptors for expired sessions cannot be removed from the cache\n"
					+ "Check that you have configured <listener-class>org.acegisecurity.ui.session.HttpSessionEventPublisher</listener-class>?\n");
		}
		
		if (cloneDescriptor != null) return cloneDescriptor;

		cloneDescriptor = new TrailsClassDescriptor(descriptor);
		cachedDescriptors.put(descriptor.getType().getSimpleName(), cloneDescriptor);
		
		List<SecurityRestriction> restrictions = securityService.findRestrictions(descriptor);
		if (restrictions != null)
		{
			for (SecurityRestriction restriction : restrictions)
			{
				restriction.restrict(context.getAuthentication().getAuthorities(), cloneDescriptor);
			}
		}
		return cloneDescriptor;
	}

	public void setSecurityService(SecurityService securityService)
	{
		this.securityService = securityService;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (!(event instanceof HttpSessionApplicationEvent)) return;
		
		if (event instanceof HttpSessionDestroyedEvent) {
			SecurityContext context = SecurityContextHolder.getContext();
			// Do nothing if security context wasn't available
			if (context == null || context.getAuthentication() == null) return;
			Object object = perUserClassDescriptorCache.remove(context.getAuthentication().getName());
			if (object != null && log.isDebugEnabled()) log.debug("Removing cached descriptors for user " + context.getAuthentication().getName());
		}
		else if (event instanceof HttpSessionCreatedEvent) sessionCreationDetected = true;
	}
}
