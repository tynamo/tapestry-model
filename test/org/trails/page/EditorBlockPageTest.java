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

    public void testGetValidator() throws Exception
    {
        IPropertyDescriptor numberDescriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
        NumberValidator validator = (NumberValidator) editorBlockPage.getValidator(numberDescriptor);
        assertEquals("right value type", Double.class,
            validator.getValueTypeClass());
        IPropertyDescriptor descriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class); 
        descriptor.setLength(55);
        descriptor.setRequired(true);
        
        StringValidator stringValidator = 
            (StringValidator)editorBlockPage.getValidator(descriptor);
        assertTrue("is required", stringValidator.isRequired());
        
    }

    public void testGetTranslator() throws Exception
    {
        IPropertyDescriptor numberDescriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
        Translator translator =  editorBlockPage.getTranslator(numberDescriptor); 
        assertTrue(translator instanceof NumberTranslator);
        assertNotNull(((FormatTranslator)translator).getPattern());
        
        IPropertyDescriptor dateDescriptor = new TrailsPropertyDescriptor(Foo.class, "date", Date.class);
        translator = editorBlockPage.getTranslator(dateDescriptor);
        assertNotNull(((FormatTranslator)translator).getPattern());
        dateDescriptor.setFormat("MM/dd/yyyy");
        translator = editorBlockPage.getTranslator(dateDescriptor);
        assertTrue(translator instanceof DateTranslator);
        assertEquals("MM/dd/yyyy", 
                ((DateTranslator)translator).getPattern());
        
    }
    
    public void testGetSelectionModel() throws Exception
    {
        IClassDescriptor barDescriptor = new TrailsClassDescriptor(IBar.class);
        IdentifierDescriptor idDescriptor = new IdentifierDescriptor(Foo.class, "id", Integer.class);
        barDescriptor.getPropertyDescriptors().add(idDescriptor);
        
        IPropertyDescriptor barPropDescriptor = 
            new TrailsPropertyDescriptor(Foo.class, "bar", IBar.class);
        
        List instances = new ArrayList();
        instances.add(new Bar());
        persistenceMock.expects(atLeastOnce()).method("getAllInstances")
                       .with(same(IBar.class)).will(returnValue(instances));
        descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor")
                    .with(same(IBar.class)).will(returnValue(barDescriptor));

        barPropDescriptor.setRequired(false);
        IPropertySelectionModel selectionModel = editorBlockPage.getSelectionModel(barPropDescriptor);
        assertEquals("1 bar and none option", 2, selectionModel.getOptionCount());
        
        barPropDescriptor.setRequired(true);
        selectionModel = editorBlockPage.getSelectionModel(barPropDescriptor);
        assertEquals("now only 1", 1, selectionModel.getOptionCount());
        persistenceMock.verify();
        
    }
    
    public void testPushCallBack()
    {
        editorBlockPage.setEditPageName("FooEdit");
        editorBlockPage.setModel(new Foo());
        editorBlockPage.pushCallback();
        assertEquals(EditCallback.class, callbackStack.get(0).getClass());
        EditCallback callback = (EditCallback)callbackStack.pop();
        assertEquals("FooEdit", callback.getPageName());
    }
}
