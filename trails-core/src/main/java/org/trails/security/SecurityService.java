package org.trails.security;

import java.util.List;

import org.trails.descriptor.IClassDescriptor;

public interface SecurityService
{

	List findRestrictions(IClassDescriptor classDescriptor);

}
