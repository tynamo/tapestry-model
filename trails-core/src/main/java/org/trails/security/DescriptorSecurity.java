package org.trails.security;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author topping
 * @date Aug 30, 2006 2:04:09 PM
 */
@Aspect
public class DescriptorSecurity {
    private SecurityService securityService;

    @Around("execution(public org.trails.descriptor.IClassDescriptor org.trails.descriptor.DescriptorService+.getClassDescriptor(Class))")
    public Object classDescriptorSecurity(ProceedingJoinPoint pjp) throws Throwable {
        IClassDescriptor desc = (IClassDescriptor) pjp.proceed();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null || context.getAuthentication() == null) {
            return desc;
        }
        /* we have to return a copy so we don't have problems with multi-threads */
        IClassDescriptor newDescriptor = new TrailsClassDescriptor(desc);
        applyRestrictions(newDescriptor, context);
        return newDescriptor;
    }

    @Around("execution(public java.util.List org.trails.descriptor.DescriptorService+.getAllDescriptors())")
    public Object getAllClassDescriptorSecurity(ProceedingJoinPoint pjp) throws Throwable {
        List<IClassDescriptor> descriptors = (List<IClassDescriptor>) pjp.proceed();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null || context.getAuthentication() == null) {
            return descriptors;
        }

        if (descriptors != null) {
            List<IClassDescriptor> newDescriptors = new ArrayList<IClassDescriptor>(descriptors.size());
            for (IClassDescriptor descriptor : descriptors) {
                IClassDescriptor newDescriptor = new TrailsClassDescriptor(descriptor);
                applyRestrictions(newDescriptor, context);
                newDescriptors.add(newDescriptor);
            }
            return newDescriptors;
        }

        return null;
    }

    protected void applyRestrictions(IClassDescriptor newDescriptor, SecurityContext context) {
        List restrictions = securityService.findRestrictions(newDescriptor);
        if (restrictions != null) {
            for (Object restriction1 : restrictions) {
                SecurityRestriction restriction = (SecurityRestriction) restriction1;
                restriction.restrict(context.getAuthentication().getAuthorities(), newDescriptor);
            }
        }
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
