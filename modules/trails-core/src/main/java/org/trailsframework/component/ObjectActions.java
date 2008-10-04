/*
 * Created on Dec 11, 2004
 *
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

import org.trails.descriptor.IMethodDescriptor;
import org.trails.page.ModelPage;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectActions extends AbstractObjectRenderComponent
{

	public List<IMethodDescriptor> getMethodDescriptors()
	{
		List<IMethodDescriptor> displayingPropertyDescriptors = new ArrayList<IMethodDescriptor>();
		for (IMethodDescriptor methodDescriptor : getClassDescriptor().getMethodDescriptors())
		{
			if (!methodDescriptor.isHidden())
			{
				displayingPropertyDescriptors.add(methodDescriptor);
			}
		}
		return displayingPropertyDescriptors;
	}

	public boolean isShowRemoveButton()
	{
		ModelPage page = (ModelPage) getPage();
		return page != null && page.getClassDescriptor().isAllowRemove() && !page.isModelNew();
	}

}
