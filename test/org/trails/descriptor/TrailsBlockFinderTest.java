package org.trails.descriptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.util.ComponentAddress;
import org.jmock.cglib.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.test.Foo;

public class TrailsBlockFinderTest extends MockObjectTestCase
{	
    public TrailsBlockFinderTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

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
    	Map pageComponents = new HashMap();
        Mock pageMock = new Mock(IPage.class);
        Mock cycleMock = new Mock(IRequestCycle.class);

    	Block overrriddenBlock = (Block)creator.newInstance(Block.class, new Object[] {"page", pageMock.proxy()});
    	overrriddenBlock.setContainer((IPage)pageMock.proxy());
    	Block block = (Block)creator.newInstance(Block.class, new Object[] {"page", pageMock.proxy()});
    	block.setContainer((IPage)pageMock.proxy());
    	pageComponents.put("overridden", overrriddenBlock);
    	pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(pageComponents));
    	pageMock.expects(atLeastOnce()).method("getComponent").with(eq("overridden")).will(returnValue(overrriddenBlock));
        
        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("trails:Editors")).will(returnValue(pageMock.proxy()));
        cycleMock.expects(atLeastOnce()).method("getPage").withNoArguments().will(returnValue(pageMock.proxy()));
        pageMock.expects(atLeastOnce()).method("getRequestCycle").will(returnValue(cycleMock.proxy()));
        pageMock.expects(atLeastOnce()).method("getNestedComponent").with(eq("stringEditor")).will(returnValue(block));
        pageMock.expects(atLeastOnce()).method("setProperty");
        pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("trails:Editors"));
    	Foo model = new Foo();
    	pageMock.expects(atLeastOnce()).method("getProperty").with(eq("model")).will(returnValue(model));
    	
        assertEquals(overrriddenBlock, editorService.findBlock((IRequestCycle)cycleMock.proxy(), overriddenDescriptor));
        
        assertEquals(block, editorService.findBlock((IRequestCycle)cycleMock.proxy(), stringDescriptor));
        
    }
    
    public void testFromSpring()
    {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(
            "applicationContext-test.xml");
        BlockFinder editorService = (BlockFinder)appContext.getBean("editorService");
        IPropertyDescriptor stringDescriptor = new TrailsPropertyDescriptor(Foo.class, "string", String.class);
        assertNotNull(editorService.findBlockAddress(stringDescriptor));
        assertTrue(editorService.findBlockAddress(stringDescriptor) instanceof ComponentAddress);
        stringDescriptor.setReadOnly(true);
        ComponentAddress editorAddress = editorService.findBlockAddress(stringDescriptor);
        assertEquals("readOnly", editorAddress.getIdPath());
    }

}
