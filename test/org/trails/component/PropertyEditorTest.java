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
package org.trails.component;


import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.StringValidator;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import org.trails.descriptor.EditorService;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyEditorTest extends ComponentTest
{
    
    PropertyEditor propertyEditor;
    IPropertyDescriptor descriptor;
    Mock editSvcMock;
    
    public void setUp() throws Exception
    {
        editSvcMock = new Mock(EditorService.class);
        propertyEditor = (PropertyEditor) creator.newInstance(PropertyEditor.class, 
                new Object[] {"editorService", editSvcMock.proxy()});
        descriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
        propertyEditor.setDescriptor(descriptor);
    }
    
    public void testGetEditorAddress()
    {
        ComponentAddress componentAddress = new ComponentAddress("page", "block");
        IPropertyDescriptor descriptor2 = new TrailsPropertyDescriptor(Foo.class, "stuff", String.class);
        editSvcMock.expects(atLeastOnce()).method("findEditor")
            .with(eq(descriptor)).will(returnValue(componentAddress));
        editSvcMock.expects(atLeastOnce()).method("findEditor")
            .with(eq(descriptor2)).will(returnValue(null));
        assertEquals(componentAddress, propertyEditor.getEditorAddress());
        
        propertyEditor.setDescriptor(descriptor2);
        assertNull(propertyEditor.getEditorAddress());
    }
    
    public void testGetBlock() throws Exception
    {
        
        Mock pageMock = new Mock(IPage.class);
        Mock cycleMock = new Mock(IRequestCycle.class);
        Block block = (Block)creator.newInstance(Block.class, new Object[] {"page", pageMock.proxy()});
        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("page")).will(returnValue(pageMock.proxy()));
        pageMock.expects(atLeastOnce()).method("getRequestCycle").will(returnValue(cycleMock.proxy()));
        pageMock.expects(atLeastOnce()).method("getNestedComponent").with(eq("block")).will(returnValue(block));
        pageMock.expects(atLeastOnce()).method("setProperty");
        pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("whatever"));
        propertyEditor.setPage((IPage)pageMock.proxy());
        
        ComponentAddress componentAddress = new ComponentAddress("page", "block");
        editSvcMock.expects(atLeastOnce()).method("findEditor")
            .with(eq(descriptor)).will(returnValue(componentAddress));
        
        assertEquals(block, propertyEditor.getBlock());
    }
   
}
