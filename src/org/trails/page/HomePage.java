/*
 * Created on 20/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 *
 */
package org.trails.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.html.BasePage;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;

public abstract class HomePage extends TrailsPage
{

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
