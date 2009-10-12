package org.tynamo.security.annotation;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.tynamo.descriptor.annotation.AbstractAnnotationHandler;
import org.tynamo.security.ClassSecurityRestriction;
import org.tynamo.security.PropertySecurityRestriction;
import org.tynamo.security.RestrictionType;

public class SecurityAnnotationHandler extends AbstractAnnotationHandler
{

	public SecurityAnnotationHandler()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public List buildClassRestrictions(Class type)
	{
		// TODO refactor the build methods with better reusability
		ArrayList<ClassSecurityRestriction> classRestrictions = new ArrayList<ClassSecurityRestriction>();
		
		if (type.getAnnotation(ViewRequiresRole.class) != null) {
			ClassSecurityRestriction classRestriction = new ClassSecurityRestriction();
			classRestriction.setRequiredRole( ((ViewRequiresRole)type.getAnnotation(ViewRequiresRole.class)).value()) ;
			classRestriction.setRestrictionType(RestrictionType.VIEW);
			classRestrictions.add(classRestriction);
		}
		if (type.getAnnotation(UpdateRequiresRole.class) != null) {
			ClassSecurityRestriction classRestriction = new ClassSecurityRestriction();
			classRestriction.setRequiredRole( ((UpdateRequiresRole)type.getAnnotation(UpdateRequiresRole.class)).value()) ;
			classRestriction.setRestrictionType(RestrictionType.UPDATE);
			classRestrictions.add(classRestriction);
		}
		if (type.getAnnotation(RemoveRequiresRole.class) != null) {
			ClassSecurityRestriction classRestriction = new ClassSecurityRestriction();
			classRestriction.setRequiredRole( ((RemoveRequiresRole)type.getAnnotation(RemoveRequiresRole.class)).value()) ;
			classRestriction.setRestrictionType(RestrictionType.REMOVE );
			classRestrictions.add(classRestriction);
		}
		
		/*
		for (int i = 0; i < restrictionsLength; i++)
		{
			ClassSecurityRestriction classRestriction = new ClassSecurityRestriction();
			setPropertiesFromAnnotation(securityAnnotation.restrictions()[i], classRestriction);
			classRestrictions.add(classRestriction);
		}
		*/
		
		
		return classRestrictions;
	}

	public List<PropertySecurityRestriction> buildPropertyRestrictions(AnnotatedElement annotatedElement, String propertyName)
	{
		ArrayList<PropertySecurityRestriction> propertyRestrictions = new ArrayList<PropertySecurityRestriction>();
		if (annotatedElement.getAnnotation(ViewRequiresRole.class) != null) {
			PropertySecurityRestriction propertyRestriction = new PropertySecurityRestriction();
			propertyRestriction.setPropertyName(propertyName);
			propertyRestriction.setRestrictionType(RestrictionType.VIEW);
			propertyRestriction.setRequiredRole(annotatedElement.getAnnotation(ViewRequiresRole.class).value());
			propertyRestrictions.add(propertyRestriction);
		}
		if (annotatedElement.getAnnotation(UpdateRequiresRole.class) != null) {
			PropertySecurityRestriction propertyRestriction = new PropertySecurityRestriction();
			propertyRestriction.setPropertyName(propertyName);
			propertyRestriction.setRestrictionType(RestrictionType.UPDATE);
			propertyRestriction.setRequiredRole(annotatedElement.getAnnotation(UpdateRequiresRole.class).value());
			propertyRestrictions.add(propertyRestriction);
		}
		if (annotatedElement.getAnnotation(RemoveRequiresRole.class) != null) {
			PropertySecurityRestriction propertyRestriction = new PropertySecurityRestriction();
			propertyRestriction.setPropertyName(propertyName);
			propertyRestriction.setRestrictionType(RestrictionType.REMOVE);
			propertyRestriction.setRequiredRole(annotatedElement.getAnnotation(RemoveRequiresRole.class).value());
			propertyRestrictions.add(propertyRestriction);
		}
		
		/* The old way is clearly shorter, but the new is easier for the user
		for (int i = 0; i < securityAnnotation.restrictions().length; i++)
		{
			PropertySecurityRestriction propertyRestriction = new PropertySecurityRestriction();
			propertyRestriction.setPropertyName(propertyName);
			setPropertiesFromAnnotation(securityAnnotation.restrictions()[i], propertyRestriction);
			propertyRestrictions.add(propertyRestriction);
		}
		*/
		return propertyRestrictions;
	}

}
