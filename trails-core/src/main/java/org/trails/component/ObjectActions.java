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

import java.beans.MethodDescriptor;

import org.trails.page.ModelPage;

/**
 * @author fus8882
 */
public abstract class ObjectActions extends ObjectEditComponent
{
	public abstract MethodDescriptor getMethodDescriptor();

	public abstract void setMethodDescriptor(MethodDescriptor MethodDescriptor);

	public boolean isShowRemoveButton()
	{
		ModelPage page = (ModelPage) getPage();
		if (page != null && page instanceof ModelPage)
		{
			return page.getClassDescriptor().isAllowRemove() && !page.isModelNew();
		} else
		{
			return false;
		}
	}

}
