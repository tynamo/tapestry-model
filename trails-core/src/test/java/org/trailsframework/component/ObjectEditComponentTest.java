/*
 * Created on Jan 11, 2005
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

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.jmock.Mock;
import org.trails.test.Foo;

import java.util.HashMap;
import java.util.Map;

public class ObjectEditComponentTest extends ComponentTest
{

	Mock pageMock;
	AbstractObjectRenderComponent editComponent;
	Mock cycleMock;
	Map<String, Block> thisPageComponents = new HashMap<String, Block>();
	IRequestCycle cycle;

	public void setUp()
	{
		editComponent = (AbstractObjectRenderComponent) creator.newInstance(AbstractObjectRenderComponent.class);
		editComponent.setModel(new Foo());

		// the page we are on
		pageMock = new Mock(IPage.class);

		editComponent.setPage((IPage) pageMock.proxy());
		// the page for the object

		cycleMock = new Mock(IRequestCycle.class);
		cycle = (IRequestCycle) cycleMock.proxy();

		pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(thisPageComponents));
	}

	public void testHasBlock()
	{
		thisPageComponents.put("blah2", (Block) creator.newInstance(Block.class));

		assertFalse("one thats not there", editComponent.hasBlock("qwerqrew"));
		assertTrue("found block on this page", editComponent.hasBlock("blah2"));
	}


	public void testGetBlock() throws Exception
	{
		Block block2 = (Block) creator.newInstance(Block.class);
		thisPageComponents.put("block", block2);

		pageMock.expects(atLeastOnce()).method("getComponent").with(eq("block")).will(returnValue(block2));

		assertEquals("right block", block2, editComponent.getBlock("block"));
		assertNull("no block", editComponent.getBlock("otherblock"));
	}
}
