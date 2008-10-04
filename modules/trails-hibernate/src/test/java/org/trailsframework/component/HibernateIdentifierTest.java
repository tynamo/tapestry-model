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
package org.trailsframework.component;


import org.trails.descriptor.IdentifierDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;
import org.trails.testhibernate.Bar;
import org.trails.testhibernate.Foo;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateIdentifierTest extends HibernateComponentTest
{
	public void testIsEditable() throws Exception
	{

/*
		HibernateIdentifier identifier = (HibernateIdentifier) creator.newInstance(HibernateIdentifier.class);
		Foo foo = new Foo();
		identifier.setModel(foo);
		IdentifierDescriptor descriptor = new IdentifierDescriptor(Foo.class, "id", Integer.class);
		descriptor.setGenerated(false);
		identifier.setDescriptor(descriptor);
		assertTrue("is editable", identifier.isEditable());
		((HasAssignedIdentifier) foo).onInsert(new Object[]{"myName"}, new String[]{"name"}, null);
		assertFalse("not editable", identifier.isEditable());
		descriptor.setGenerated(true);
		identifier.setModel(new Bar());
		assertFalse("not editable", identifier.isEditable());
*/

	}
}