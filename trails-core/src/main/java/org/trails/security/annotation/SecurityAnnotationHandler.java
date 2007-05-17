package org.trails.security.annotation;

import java.util.ArrayList;
import java.util.List;

import org.trails.descriptor.annotation.AbstractAnnotationHandler;
import org.trails.security.ClassSecurityRestriction;
import org.trails.security.PropertySecurityRestriction;

public class SecurityAnnotationHandler extends AbstractAnnotationHandler
{

	public SecurityAnnotationHandler()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public List buildClassRestrictions(Security securityAnnotation)
	{
		/* I got some null pointers here when testing DescriptorSecurity aspect */
		Restriction[] restrictions = (securityAnnotation == null)
			? null
			: securityAnnotation.restrictions();
		int restrictionsLength = (restrictions == null)
			? 0
			: restrictions.length;
		ArrayList classRestrictions = new ArrayList();
		for (int i = 0; i < restrictionsLength; i++)
		{
			ClassSecurityRestriction classRestriction = new ClassSecurityRestriction();
			setPropertiesFromAnnotation(securityAnnotation.restrictions()[i], classRestriction);
			classRestrictions.add(classRestriction);
		}
		return classRestrictions;
	}

	public List buildPropertyRestrictions(Security securityAnnotation, String propertyName)
	{
		ArrayList propertyRestrictions = new ArrayList();
		for (int i = 0; i < securityAnnotation.restrictions().length; i++)
		{
			PropertySecurityRestriction propertyRestriction = new PropertySecurityRestriction();
			propertyRestriction.setPropertyName(propertyName);
			setPropertiesFromAnnotation(securityAnnotation.restrictions()[i], propertyRestriction);
			propertyRestrictions.add(propertyRestriction);
		}
		return propertyRestrictions;
	}

}
