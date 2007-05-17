/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.component;

import java.beans.MethodDescriptor;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.trails.TrailsRuntimeException;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class InvokeMethod extends TrailsComponent
{
	public abstract MethodDescriptor getMethodDescriptor();

	public abstract void setMethodDescriptor(MethodDescriptor MethodDescriptor);

	public abstract IActionListener getListener();

	public abstract void setListener(IActionListener Listener);

	public abstract Object getModel();

	public abstract void setModel(Object Model);

	public void click(IRequestCycle cycle)
	{
		try
		{
			getMethodDescriptor().getMethod().invoke(getModel(),
				new Object[]{});
		} catch (Exception e)
		{
			throw new TrailsRuntimeException(e);
		}

		if (getListener() != null)
		{
			getListener().actionTriggered(this, cycle);
		}
	}
}
