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
package org.trailsframework.page;

import org.apache.tapestry.IRequestCycle;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jmock.Mock;
import org.trails.component.HibernateComponentTest;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.testhibernate.Bar;
import org.trails.testhibernate.Baz;
import org.trails.testhibernate.Foo;
import org.trails.validation.HibernateValidationDelegate;


public class HibernateEditPageTest extends HibernateComponentTest
{
	Mock cycleMock = new Mock(IRequestCycle.class);
	HibernateEditPage editPage;
	Foo foo = new Foo();
	IClassDescriptor fooDescriptor = new TrailsClassDescriptor(Foo.class, "Foo");

	protected void setUp() throws Exception
	{
		super.setUp();

		persistenceMock = new Mock(HibernatePersistenceService.class);  // @todo: remove when the components reuse issue goes away
		delegate = new HibernateValidationDelegate();

		foo.setName("foo");

		editPage = buildEditPage();
		editPage.setClassDescriptor(fooDescriptor);
		editPage.setModel(foo);
	}

	public void testSaveWithInvalidStateException() throws Exception
	{
		InvalidValue invalidValue = new InvalidValue("is too long", Bar.class, "description", "blarg", new Baz());
		InvalidStateException invalidStateException = new InvalidStateException(new InvalidValue[]{invalidValue});
		persistenceMock.expects(once()).method("save").with(same(foo)).will(throwException(invalidStateException));
		editPage.save((IRequestCycle) cycleMock.proxy());
		assertTrue("delegate has errors", delegate.getHasErrors());
	}
}