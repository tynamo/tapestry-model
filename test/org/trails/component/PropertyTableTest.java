/*
 * Created on Jan 4, 2005
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

import java.util.Date;

import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyTableTest extends ComponentTest
{

    public void testGetPropertyDescriptors() throws Exception
    {
        PropertyTable propertyTable = (PropertyTable)creator.newInstance(PropertyTable.class);
        
        IClassDescriptor classDescriptor = new TrailsClassDescriptor(Foo.class);
        IPropertyDescriptor nameDescriptor = new TrailsPropertyDescriptor(Foo.class, 
            "name", String.class);
        IPropertyDescriptor dateDescriptor = new TrailsPropertyDescriptor(Foo.class, 
            "date", Date.class);
        IPropertyDescriptor numberDescriptor = new TrailsPropertyDescriptor(Foo.class,
            "nubmer", Double.class);
        classDescriptor.getPropertyDescriptors().add(nameDescriptor);
        classDescriptor.getPropertyDescriptors().add(dateDescriptor);
        classDescriptor.getPropertyDescriptors().add(numberDescriptor);
        propertyTable.setClassDescriptor(classDescriptor);
        assertEquals("got 3", 3, propertyTable.getPropertyDescriptors().size());
        nameDescriptor.setHidden(true);
        assertEquals("got 2", 2, propertyTable.getPropertyDescriptors().size());
        assertFalse(propertyTable.getPropertyDescriptors().contains(nameDescriptor));
        
        propertyTable.setPropertyNames(new String[] {"date", "name"});
        assertTrue(propertyTable.getPropertyDescriptors().contains(nameDescriptor));
        assertEquals("got 2", 2, propertyTable.getPropertyDescriptors().size());
        assertEquals("first one is date", dateDescriptor, 
            propertyTable.getPropertyDescriptors().get(0));
    }
}
