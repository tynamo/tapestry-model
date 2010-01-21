package org.tynamo.descriptor;

import java.util.HashMap;

import org.apache.tapestry.util.ComponentAddress;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tynamo.testhibernate.Foo;
import org.tynamo.finder.EditorBlockFinder;
import org.tynamo.finder.BlockFinder;

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

	public void testFromSpring()
	{
		ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");
		BlockFinder editorService = (BlockFinder) appContext.getBean("editorService");
		IPropertyDescriptor stringDescriptor = new TrailsPropertyDescriptor(Foo.class, "string", String.class);
		assertNotNull(editorService.findBlockAddress(stringDescriptor));
		assertTrue(editorService.findBlockAddress(stringDescriptor) instanceof ComponentAddress);
		stringDescriptor.setReadOnly(true);
		ComponentAddress editorAddress = editorService.findBlockAddress(stringDescriptor);
		assertEquals("readOnly", editorAddress.getIdPath());
		IPropertyDescriptor passwordDescriptor = new TrailsPropertyDescriptor(Foo.class, "password", String.class);
		editorAddress = editorService.findBlockAddress(passwordDescriptor);
		assertEquals("passwordEditor", editorAddress.getIdPath());
	}


}