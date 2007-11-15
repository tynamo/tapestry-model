/*
 * Created on 20/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.trails.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.descriptor.IClassDescriptor;

public abstract class HomePage extends TrailsPage implements IExternalPage
{

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
	{
		getCallbackStack().clear();
	}

	public List<IClassDescriptor> getAllDescriptors()
	{
		List<IClassDescriptor> descriptors = getDescriptorService().getAllDescriptors();
		List<IClassDescriptor> result = new ArrayList<IClassDescriptor>(descriptors.size());

		for (IClassDescriptor descriptor : descriptors)
		{
			if (!descriptor.isHidden())
			{
				result.add(descriptor);
			}
		}

		return result;
	}

}
