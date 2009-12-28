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
package org.tynamo.component;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;

import ognl.Ognl;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.tynamo.test.Baz;
import org.tynamo.descriptor.TrailsMethodDescriptor;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class InvokeMethodTest extends ComponentTest
{

	InvokeMethod invokeMethod;
	Baz baz;
	BeanInfo beanInfo;
	Mock listenerMock;
	IRequestCycle cycle;
	Mock cycleMock;

	/*
		 * @see TestCase#setUp()
		 */
	public void setUp() throws Exception
	{
		super.setUp();
		beanInfo = Introspector.getBeanInfo(Baz.class);
		invokeMethod = (InvokeMethod) creator.newInstance(InvokeMethod.class);
		baz = new Baz();
		invokeMethod.setModel(baz);
		listenerMock = new Mock(IActionListener.class);
		cycleMock = new Mock(IRequestCycle.class);
		cycle = (IRequestCycle) cycleMock.proxy();
		listenerMock.expects(once()).method("actionTriggered").with(same(invokeMethod),
			same(cycle));
		invokeMethod.setListener((IActionListener) listenerMock.proxy());
	}

	public void testClick() throws Exception
	{
		MethodDescriptor beanMethodDescriptor =
				(MethodDescriptor) Ognl.getValue("methodDescriptors.{? name == 'doSomething'}[0]", beanInfo);

		TrailsMethodDescriptor trailsMethodDescriptor = new TrailsMethodDescriptor(Baz.class,
				beanMethodDescriptor.getMethod().getName(), beanMethodDescriptor.getMethod().getReturnType(),
				beanMethodDescriptor.getMethod().getParameterTypes());

		invokeMethod.setMethodDescriptor(trailsMethodDescriptor);

		invokeMethod.click(cycle);
		assertEquals("method invokes", "something done", baz.getDescription());
		listenerMock.verify();

		// test w/parameters
	}

	//    public void testWithParameters() throws Exception
	//    {
	//        MethodDescriptor methodWParams = (MethodDescriptor)
	//    	Ognl.getValue("methodDescriptors.{? name == 'doSomethingElse'}[0]", beanInfo);
	//        	invokeMethod.setMethodDescriptor(methodWParams);
	//        InvokeMethodPage invokeMethodPage = (InvokeMethodPage)creator.newInstance(InvokeMethodPage.class);
	//        cycleMock.expects(once()).method("getPage").with(same("Baz_doSomethingElse"))
	//        	.will(returnValue(invokeMethodPage));
	//        cycleMock.expects(once()).method("activate").with(same(invokeMethodPage));
	//        invokeMethod.click((IRequestCycle)cycleMock.proxy());
	//        assertEquals("right method descriptor", methodWParams, invokeMethodPage.getMethodDescriptor());
	//        assertEquals("got model", invokeMethod.getModel(), invokeMethodPage.getModel());
	//        cycleMock.verify();
	//    }
}
