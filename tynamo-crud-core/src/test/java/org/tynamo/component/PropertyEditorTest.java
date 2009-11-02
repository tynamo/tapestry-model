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
package org.tynamo.component;


import org.apache.hivemind.Messages;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.util.ComponentAddress;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.tynamo.finder.BlockFinder;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.page.IEditorBlockPage;
import org.tynamo.test.Foo;


public class PropertyEditorTest extends MockObjectTestCase
{

	PropertyEditor propertyEditor;
	IPropertyDescriptor descriptor;
	TrailsClassDescriptor classDescriptor;
	BlockFinder blockFinder;
	Messages messages;
	Foo model;

	Creator creator = new Creator();

	public void setUp() throws Exception
	{
		messages = mock(Messages.class);
		blockFinder = mock(BlockFinder.class);
		model = new Foo();
		descriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
		classDescriptor = new TrailsClassDescriptor(Foo.class);

		propertyEditor = (PropertyEditor) creator.newInstance(PropertyEditor.class,
			new Object[]{
					"blockFinder", blockFinder,
					"messages", messages,
					"model", model,
					"modelNew", false,
					"descriptor", descriptor,
					"classDescriptor", classDescriptor

			});
	}

	public void testGetEditorAddress()
	{
		final ComponentAddress componentAddress = new ComponentAddress("page", "block");
		final IPropertyDescriptor descriptor2 = new TrailsPropertyDescriptor(Foo.class, "stuff", String.class);

		checking(new Expectations()
		{
			{
				atLeast(1).of(blockFinder).findBlockAddress(descriptor); will(returnValue(componentAddress));
				atLeast(1).of(blockFinder).findBlockAddress(descriptor2); will(returnValue(null));
			}
		});

		assertEquals(componentAddress, propertyEditor.getEditorAddress());

		propertyEditor = (PropertyEditor) creator.newInstance(PropertyEditor.class,
			new Object[]{
					"blockFinder", blockFinder,
					"messages", messages,
					"model", model,
					"modelNew", false,
					"descriptor", descriptor2,
					"classDescriptor", classDescriptor
			});
		
		assertNull(propertyEditor.getEditorAddress());
	}

	public void testGetBlock() throws Exception
	{
		final String pageName = "whatever";

		final IEditorBlockPage page = mock(IEditorBlockPage.class);
		final IRequestCycle cycle = mock(IRequestCycle.class);
		final ComponentAddress componentAddress = new ComponentAddress(pageName, "block");
		final Block block = (Block) creator.newInstance(Block.class, new Object[]{"page", page, "container", page});

		checking(new Expectations()
		{
			{
				atLeast(1).of(page).getRequestCycle(); will(returnValue(cycle));
				atLeast(1).of(cycle).getPage(pageName); will(returnValue(page));
				atLeast(1).of(page).getNestedComponent("block"); will(returnValue(block));

				atLeast(1).of(page).setModel(model);
				atLeast(1).of(page).setModelNew(false);
				atLeast(1).of(page).setDescriptor(descriptor);
				atLeast(1).of(page).setClassDescriptor(classDescriptor);

				atLeast(1).of(blockFinder).findBlockAddress(descriptor); will(returnValue(componentAddress));
			}
		});

		propertyEditor.setPage(page);
		assertEquals(block, propertyEditor.getBlock());
	}


}
