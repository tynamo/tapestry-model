package org.trails.page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.form.translator.FormatTranslator;
import org.apache.tapestry.form.translator.NumberTranslator;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.StringValidator;
import org.trails.callback.EditCallback;
import org.trails.component.ComponentTest;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;
import org.trails.test.Bar;
import org.trails.test.Foo;
import org.trails.test.IBar;

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
