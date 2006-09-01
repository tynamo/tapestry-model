package org.trails.page;

import org.trails.callback.EditCallback;
import org.trails.component.ComponentTest;
import org.trails.test.Foo;

public class EditorBlockPageTest extends ComponentTest
{
    EditorBlockPage editorBlockPage;
    
    public void setUp()
    {
        editorBlockPage = buildTrailsPage(EditorBlockPage.class);
    }
    
    public EditorBlockPageTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }


    
    public void testPushCallBack()
    {
    	Foo foo = new Foo();
        editorBlockPage.setEditPageName("FooEdit");
        editorBlockPage.setModel(foo);
        
        editorBlockPage.pushCallback();
        assertEquals(EditCallback.class, callbackStack.getStack().get(0).getClass());
        EditCallback callback = (EditCallback)callbackStack.getStack().pop();
        assertEquals("FooEdit", callback.getPageName());
    }
}
