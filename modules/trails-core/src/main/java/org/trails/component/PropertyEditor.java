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

import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.page.IEditorBlockPage;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class PropertyEditor extends TrailsComponent
{
	public abstract IPropertyDescriptor getDescriptor();

	public abstract void setDescriptor(IPropertyDescriptor Descriptor);

	public abstract Object getModel();

	public abstract void setModel(Object model);

	public abstract BlockFinder getBlockFinder();

	public Block getBlock()
	{

		Block editorBlock = (Block)
			getEditorAddress().findComponent(getPage().getRequestCycle());

		((IEditorBlockPage) editorBlock.getPage()).setModel(getModel());
		((IEditorBlockPage) editorBlock.getPage()).setDescriptor(getDescriptor());
		((IEditorBlockPage) editorBlock.getPage()).setEditPageName(getPage().getPageName());

		return editorBlock;
	}

	public ComponentAddress getEditorAddress()
	{

		return getBlockFinder().findBlockAddress(getDescriptor());
	}
}
