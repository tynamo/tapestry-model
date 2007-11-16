/*
 * Created on Mar 15, 2005
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
package org.trails.page;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

public abstract class TrailsPage extends BasePage implements IActivatableTrailsPage, PageBeginRenderListener
{

	public void pushCallback()
	{
	}

	public void activateTrailsPage(Object[] objects, IRequestCycle iRequestCycle)
	{
		pushCallback();
	}

	public void pageBeginRender(PageEvent event)
	{
		Defense.notNull(getClassDescriptor(), "classDescriptor");
	}
}
