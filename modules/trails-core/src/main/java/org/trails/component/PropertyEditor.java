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

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.components.RenderBlock;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.page.IEditorBlockPage;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class PropertyEditor extends AbstractComponent
{

	@Parameter(defaultValue = "container.property")
	public abstract IPropertyDescriptor getDescriptor();

	@Parameter(defaultValue = "container.model")
	public abstract Object getModel();

	@Parameter(defaultValue = "container.modelNew")
	public abstract boolean isModelNew();

	@Parameter(defaultValue = "container.blockFinder")
	public abstract BlockFinder getBlockFinder();

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
	{
		getRenderBlock().render(writer, cycle);
	}

	public Block getBlock()
	{

		Block editorBlock = (Block)
				getEditorAddress().findComponent(getPage().getRequestCycle());

		((IEditorBlockPage) editorBlock.getPage()).setModel(getModel());
		((IEditorBlockPage) editorBlock.getPage()).setModelNew(isModelNew());
		((IEditorBlockPage) editorBlock.getPage()).setDescriptor(getDescriptor());

		return editorBlock;
	}

	public ComponentAddress getEditorAddress()
	{

		return getBlockFinder().findBlockAddress(getDescriptor());
	}

	@Component(bindings = "block=ognl:block")
	public abstract RenderBlock getRenderBlock();

}
