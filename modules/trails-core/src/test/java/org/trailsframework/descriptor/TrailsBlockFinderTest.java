package org.trails.descriptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.util.ComponentAddress;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.trails.page.IEditorBlockPage;
import org.trails.test.Foo;
import org.trails.finder.EditorBlockFinder;

public class TrailsBlockFinderTest extends MockObjectTestCase
{

	IPropertyDescriptor descriptor;
	IPropertyDescriptor stringDescriptor;
	IPropertyDescriptor fooDescriptor;
	IPropertyDescriptor overriddenDescriptor;
	ComponentAddress readOnlyEditor;
	ComponentAddress numericEditor;
	ComponentAddress stringEditor;
	EditorBlockFinder editorService = new EditorBlockFinder();

	public void setUp() throws Exception
	{
		HashMap editorMap = new HashMap();
		readOnlyEditor = new ComponentAddress("trails:Editors", "readOnlyEditor");
		numericEditor = new ComponentAddress("trails:Editors", "numericEditor");
		stringEditor = new ComponentAddress("trails:Editors", "stringEditor");
		editorService.setDefaultBlockAddress(stringEditor);
		editorMap.put("numeric", numericEditor);
		editorMap.put("string", stringEditor);

		editorService.setEditorMap(editorMap);
		descriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
		stringDescriptor = new TrailsPropertyDescriptor(Foo.class, "string", String.class);
		fooDescriptor = new TrailsPropertyDescriptor(Foo.class, "foo", Foo.class);
		overriddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "overridden", String.class);

	}

	public void testFindBlockAddress()
	{

		assertEquals(numericEditor, editorService.findBlockAddress(descriptor));
		assertEquals(stringEditor, editorService.findBlockAddress(stringDescriptor));
		assertEquals("got default editor",
			stringEditor, editorService.findBlockAddress(fooDescriptor));
		stringDescriptor.setReadOnly(true);

	}

	public void testFindBlock()
	{

		Creator creator = new Creator();

		final IEditorBlockPage page = mock(IEditorBlockPage.class);
		final IRequestCycle cycle = mock(IRequestCycle.class);
		final Foo model = new Foo();

		final Block overrriddenBlock = (Block) creator.newInstance(Block.class, new Object[]{"page", page, "container", page});

		final Block block = (Block) creator.newInstance(Block.class, new Object[]{"page", page, "container", page});

		final Map pageComponents = new HashMap();
		pageComponents.put("overridden", overrriddenBlock);

		checking(new Expectations() {{

			atLeast(1).of(page).getComponents(); will(returnValue(pageComponents));
			atLeast(1).of(page).getComponent("overridden"); will(returnValue(overrriddenBlock));


			atLeast(1).of(page).setModel(model);
			atLeast(1).of(page).getModel(); will(returnValue(model));
			one(page).setDescriptor(stringDescriptor);
			one(page).getNestedComponent("stringEditor"); will(returnValue(block));

			atLeast(1).of(cycle).getPage(); will(returnValue(page));
			atLeast(1).of(cycle).getPage("trails:Editors"); will(returnValue(page));
		}});

		assertEquals(overrriddenBlock, editorService.findBlock(cycle, overriddenDescriptor));
		assertEquals(block, editorService.findBlock(cycle, stringDescriptor));
	}
}
