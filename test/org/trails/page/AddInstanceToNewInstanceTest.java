/*
 * Created on Feb 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.trails.page;

import java.util.Set;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.jmock.Mock;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import org.trails.component.ComponentTest;
import org.trails.component.EditCollection;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.test.A;
import org.trails.test.B;
import org.trails.test.C;
import org.trails.test.Foo;

/**
 * @author Mark Newman
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AddInstanceToNewInstanceTest extends ComponentTest
{

    private Mock engineMock;

    private Mock cycleMock;

    private EditPage aEditPage;

    private EditCollection editCollection;

    private EditPage bEditPage;

    private EditPage cEditPage;
    
    private EditorBlockPage editorBlockPage;
    /*
     * @see TestCase#setUp()
     */
    public void setUp() throws Exception
    {
        editorBlockPage = buildTrailsPage(EditorBlockPage.class);
        
        engineMock = new Mock(IEngine.class);
        cycleMock = new Mock(IRequestCycle.class);
        Mock delegateMock = new Mock(IValidationDelegate.class);
        delegateMock.expects(atLeastOnce()).method("getHasErrors").will(returnValue(false));

        
        aEditPage = buildEditPage();
        aEditPage.setPageName("AEdit");
        //aEditPage.setDelegate(new TrailsValidationDelegate());
        editCollection = (EditCollection) creator
                .newInstance(EditCollection.class);
        
        
        bEditPage = buildEditPage();
        bEditPage.setPageName("BEdit");
     
        //bEditPage.setDelegate(new TrailsValidationDelegate());
        
        cEditPage = buildEditPage();
        cEditPage.setPageName("CEdit");
        
        //cEditPage.setDelegate(new TrailsValidationDelegate());
        
        super.setUp();

    }

    public void testAdd() throws Exception
    {
        persistenceMock.expects(atLeastOnce()).method("save").withAnyArguments().will(
                new Stub() 
                {

                    public Object invoke(Invocation invocation) throws Throwable
                    {
                        
                        return invocation.parameterValues.get(0);
                    }

                    public StringBuffer describeTo(StringBuffer arg0)
                    {
                        return arg0.append("returns the argument");
                    }
                    
                });
        
        //Define behavior of mock objects.

        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("AEdit")).will(
            returnValue(aEditPage));
        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("BEdit"))
                .will(returnValue(bEditPage));
        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("CEdit"))
                .will(returnValue(cEditPage));

        cycleMock.expects(atLeastOnce()).method("activate")
                .with(same(aEditPage));
        cycleMock.expects(atLeastOnce()).method("activate").with(
            same(bEditPage));
        cycleMock.expects(atLeastOnce()).method("activate").with(
            same(cEditPage));

 
        CollectionDescriptor bsDescriptor = new CollectionDescriptor(Foo.class, "bs", Set.class);
        bsDescriptor.setElementType(B.class);


        CollectionDescriptor csDescriptor = new CollectionDescriptor(Foo.class, "cs", Set.class);
        csDescriptor.setElementType(C.class);

        // Define the parent class to which we will be adding a new instance of
        // B with a new instance of C inside.

        A a = new A();
        aEditPage.setModel(a);

        editCollection.setPage(editorBlockPage);
        editorBlockPage.setEditPageName("AEdit");
        editCollection.setModel(a);
        editCollection.setCollection(a.getBs());
        editCollection.setPropertyDescriptor(bsDescriptor);
        editCollection.setCallbackStack(callbackStack);
        editCollection.showAddPage((IRequestCycle) cycleMock.proxy());
        aEditPage.onFormSubmit((IRequestCycle) cycleMock.proxy());
        
        EditCollection bEditCollection = (EditCollection)creator.newInstance(EditCollection.class);
        bEditCollection.setModel(bEditPage.getModel());
        bEditCollection.setCollection(((B) bEditPage.getModel()).getCs());
        bEditCollection.setPropertyDescriptor(csDescriptor);
        bEditCollection.setCallbackStack(callbackStack);
        bEditCollection.setPage(editorBlockPage);
        editorBlockPage.setEditPageName("BEdit");
        bEditCollection.showAddPage((IRequestCycle) cycleMock.proxy());
        bEditPage.onFormSubmit((IRequestCycle) cycleMock.proxy());
       
        cEditPage.saveAndReturn((IRequestCycle) cycleMock.proxy());
        bEditPage.saveAndReturn((IRequestCycle) cycleMock.proxy());

        B b = (B) a.getBs().iterator().next();
        C c = (C) b.getCs().iterator().next();

        assertEquals("edit page has a", a, aEditPage.getModel());
        assertEquals("add page has b", b, bEditPage.getModel());

        cycleMock.verify();

    }
}