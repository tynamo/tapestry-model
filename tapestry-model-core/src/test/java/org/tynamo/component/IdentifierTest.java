/*
 * Created on Dec 5, 2004
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
package org.tynamo.component;


import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.test.Bar;
import org.tynamo.test.Foo;
import org.tynamo.page.ModelPage;
import org.jmock.Mock;
import org.apache.tapestry.IPage;

public class IdentifierTest extends ComponentTest {

    public void testIsEditable() throws Exception {

/*        Mock pageMock = new Mock(ModelPage.class);
        Identifier identifier = (Identifier) creator.newInstance(Identifier.class);
        identifier.setPage((IPage) pageMock.proxy());
        Foo foo = new Foo();
        identifier.setModel(foo);
        pageMock.expects(once()).method("getModel").will(returnValue(foo));
        IdentifierDescriptor descriptor = new IdentifierDescriptor(Foo.class, "id", Integer.class);
        descriptor.setGenerated(false);
        identifier.setDescriptor(descriptor);
        assertTrue("is editable", identifier.isEditable());
        foo.setId(1);
        assertFalse("not editable", identifier.isEditable());
        descriptor.setGenerated(true);
        identifier.setModel(new Bar());
        assertFalse("not editable", identifier.isEditable());*/
    }
}
