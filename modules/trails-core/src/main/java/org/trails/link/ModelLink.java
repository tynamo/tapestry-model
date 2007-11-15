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
package org.trails.link;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.trails.component.Utils;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.PageType;

public abstract class ModelLink extends TrailsLink
{

	@Parameter(required = true)
	public abstract Object getModel();

	public IClassDescriptor getClassDescriptor()
	{
		Defense.notNull(getModel(), "model");
		return getDescriptorService().getClassDescriptor(Utils.checkForCGLIB(getModel().getClass()));
	}

	@InjectObject("spring:descriptorService")
	public abstract DescriptorService getDescriptorService();

}