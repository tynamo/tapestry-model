package org.tynamo.security;

import java.util.List;

import org.tynamo.descriptor.IClassDescriptor;

public interface SecurityService
{

	List findRestrictions(IClassDescriptor classDescriptor);

}
