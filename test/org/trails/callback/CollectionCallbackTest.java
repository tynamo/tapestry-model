/*
 * Created on Feb 28, 2005
 *
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
package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.page.EditPage;
import org.trails.test.Baz;
import org.trails.test.Foo;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollectionCallbackTest extends ComponentTest
{
    CollectionCallback callBack;
    String pageName = "fooAdd";
    Foo foo = new Foo();
    Baz baz = new Baz();
    
    public void setUp() throws Exception
    {
        callBack = new CollectionCallback(pageName, foo, "bazzes.add", "bazzes.remove");
    }
    
    public void testCallback()
    {
        EditPage addPage = (EditPage)creator.newInstance(EditPage.class);
        

        Mock cycleMock = new Mock(IRequestCycle.class);
        cycleMock.expects(once()).method("getPage").with(eq(pageName)).will(returnValue(addPage));
        cycleMock.expects(once()).method("activate").with(same(addPage));
        callBack.performCallback((IRequestCycle)cycleMock.proxy());
        
        
    }
    
    public void testAdd() throws Exception
    {
        callBack.add(baz);
        assertEquals("1 baz", 1, foo.getBazzes().size());
        assertEquals(baz, foo.getBazzes().iterator().next());
    }
    
    public void testRemove() throws Exception
    {
        foo.getBazzes().add(baz);
        callBack.remove(baz);
        assertEquals("0 bazzes", 0, foo.getBazzes().size());
    }
}
