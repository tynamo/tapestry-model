/*
 * Created on 20/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.trails.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.descriptor.IClassDescriptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class HomePage extends TrailsPage implements IExternalPage
{

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
    {
        getCallbackStack().getStack().clear();
    }

    public List getAllDescriptors()
	{
		List descriptors = getDescriptorService().getAllDescriptors();
		List result = new ArrayList(descriptors.size());

		Iterator i = descriptors.iterator();
		while (i.hasNext())
		{
			IClassDescriptor descriptor = (IClassDescriptor) i.next();
			if ( !descriptor.isHidden())
			{
				result.add(descriptor);
			}
		}

		return result;
	}

}
