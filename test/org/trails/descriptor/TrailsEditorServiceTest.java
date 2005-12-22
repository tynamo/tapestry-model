package org.trails.descriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.tapestry.util.ComponentAddress;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.test.Foo;

public class TrailsEditorServiceTest extends MockObjectTestCase
{

    public TrailsEditorServiceTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void testFindEditor()
    {
        TrailsEditorService editorService = new TrailsEditorService();
        HashMap editorMap = new HashMap();
        ComponentAddress readOnlyEditor = new ComponentAddress("trails:Editors", "readOnlyEditor");
        ComponentAddress numericEditor = new ComponentAddress("trails:Editors", "numericEditor");
        ComponentAddress stringEditor = new ComponentAddress("trails:Editors", "stringEditor");
        editorService.setDefaultEditor(stringEditor);
        editorMap.put("numeric", numericEditor);
        editorMap.put("string", stringEditor);
        
        editorService.setEditorMap(editorMap);
        IPropertyDescriptor descriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
        IPropertyDescriptor stringDescriptor = new TrailsPropertyDescriptor(Foo.class, "string", String.class);
        IPropertyDescriptor fooDescriptor = new TrailsPropertyDescriptor(Foo.class, "foo", Foo.class);
        
        assertEquals(numericEditor, editorService.findEditor(descriptor));
        assertEquals(stringEditor, editorService.findEditor(stringDescriptor));
        assertEquals("got default editor", 
                stringEditor, editorService.findEditor(fooDescriptor));
        stringDescriptor.setReadOnly(true);
        
    }
    
    public void testFromSpring()
    {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(
            "applicationContext-test.xml");
        EditorService editorService = (EditorService)appContext.getBean("editorService");
        IPropertyDescriptor stringDescriptor = new TrailsPropertyDescriptor(Foo.class, "string", String.class);
        assertNotNull(editorService.findEditor(stringDescriptor));
        assertTrue(editorService.findEditor(stringDescriptor) instanceof ComponentAddress);
        stringDescriptor.setReadOnly(true);
        ComponentAddress editorAddress = editorService.findEditor(stringDescriptor);
        assertEquals("readOnly", editorAddress.getIdPath());
    }

}
