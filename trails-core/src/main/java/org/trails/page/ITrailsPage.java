package org.trails.page;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.descriptor.IClassDescriptor;


public interface ITrailsPage extends IPage
{

	IClassDescriptor getClassDescriptor();

	void setClassDescriptor(IClassDescriptor iClassDescriptor);

	void activateTrailsPage(Object[] objects, IRequestCycle iRequestCycle);
}
