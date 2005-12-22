/*
 * Created on 14/12/2005 by Eduardo Piva - eduardo@gwe.com.br
 *
 */
package org.trails.security;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import net.sf.acegisecurity.context.SecurityContext;
import net.sf.acegisecurity.context.SecurityContextHolder;

public aspect DescriptorSecurity {
	
	private SecurityService securityService;

	pointcut classDescriptorSecurity() : execution(public IClassDescriptor org.trails.descriptor.DescriptorService+.getClassDescriptor(Class));
	
	pointcut getAllClassDescriptorSecurity() : execution(public List org.trails.descriptor.DescriptorService+.getAllDescriptors());

	Object around() : classDescriptorSecurity() {
	
		IClassDescriptor desc = (IClassDescriptor) proceed();
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null || context.getAuthentication() == null) {
			return desc;
		}
		/* we have to return a copy so we don't have problems with multi-threads */
		IClassDescriptor newDescriptor = new TrailsClassDescriptor(desc);
		applyRestrictions(newDescriptor, context);			
		return newDescriptor;
	}

	Object around() : getAllClassDescriptorSecurity() {
		List descriptors = (List) proceed();
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null || context.getAuthentication() == null) {
			return descriptors;
		}

		if (descriptors != null) {
			List newDescriptors = new ArrayList(descriptors.size());
			Iterator i = descriptors.iterator();
			while (i.hasNext()) {
				IClassDescriptor descriptor = (IClassDescriptor) i.next();
				IClassDescriptor newDescriptor = new TrailsClassDescriptor(descriptor);
				applyRestrictions(newDescriptor, context);
				newDescriptors.add(newDescriptor);
			}
			return newDescriptors;
		}
		
		return null;
	}
	
	private void applyRestrictions(IClassDescriptor newDescriptor, SecurityContext context) {
		List restrictions = securityService.findRestrictions(newDescriptor);
		if (restrictions != null) {
			Iterator i = restrictions.iterator();
			while (i.hasNext()) {
				SecurityRestriction restriction = (SecurityRestriction)i.next();
				restriction.restrict(context.getAuthentication().getAuthorities(), newDescriptor);
			}
		}
	}

	/** setter **/
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

}