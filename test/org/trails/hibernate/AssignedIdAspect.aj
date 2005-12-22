/*
 * Created on Nov 23, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.trails.hibernate;

/**
 * @author fus8882
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public aspect AssignedIdAspect
{
    declare parents : org.trails.test.Foo implements HasAssignedIdentifier;	
}