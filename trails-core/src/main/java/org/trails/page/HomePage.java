/*
 * Created on 20/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.trails.page;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;
import org.trails.callback.CallbackStack;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;

import java.util.ArrayList;
import java.util.List;

public abstract class HomePage extends BasePage implements PageBeginRenderListener
{

	public abstract CallbackStack getCallbackStack();

	@InjectObject("service:trails.core.DescriptorService")
	public abstract DescriptorService getDescriptorService();

	public void pageBeginRender(PageEvent event)
	{
		if (getCallbackStack() != null)
		{
			getCallbackStack().clear();
		}
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
