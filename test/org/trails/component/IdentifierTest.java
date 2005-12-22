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
package org.trails.component;


import org.jmock.MockObjectTestCase;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;
import org.trails.test.Bar;
import org.trails.test.Foo;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IdentifierTest extends ComponentTest
{
    public void testIsEditable() throws Exception
    {
        
        Identifier identifier = (Identifier)creator.newInstance(Identifier.class);
        Foo foo = new Foo();
        identifier.setModel(foo);
        IdentifierDescriptor descriptor = new IdentifierDescriptor(Foo.class, "id", Integer.class);
        descriptor.setGenerated(false);
        identifier.setDescriptor(descriptor);
        assertTrue("is editable", identifier.isEditable());
        ((HasAssignedIdentifier)foo).onSave();
        assertFalse("not editable", identifier.isEditable());
        descriptor.setGenerated(true);
        identifier.setModel(new Bar());
        assertFalse("not editable", identifier.isEditable());
    }
}
