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
package org.trails.hibernate;

import org.jmock.MockObjectTestCase;
import org.trails.test.Foo;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TrailsInterceptorTest extends MockObjectTestCase
{
    public void testIsUnsaved()
    {
        Foo foo = new Foo();
        Interceptable interceptable = (Interceptable) foo;
        assertFalse("not saved", interceptable.isSaved());
        interceptable.onInsert(new Object[]{"myName"}, new String[]{"name"}, null);
        assertTrue("saved", interceptable.isSaved());
    }

//    public void testSaveAssignedId()
//    {
//        Foo foo = new Foo();
//        ApplicationContext appContext = new ClassPathXmlApplicationContext(
//                "applicationContext-test.xml");
//        PersistenceService persistenceService = (PersistenceService) appContext.getBean(
//                "persistenceService");
//        foo.setId(new Integer(99));
//        foo.setName("happy");
//        persistenceService.save(foo);
//        foo = (Foo) persistenceService.getInstance(Foo.class, new Integer(99));
//        foo.setNumber(new Double(2));
//        persistenceService.save(foo);
//    }
}
