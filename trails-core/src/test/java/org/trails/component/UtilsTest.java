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

import java.lang.reflect.Method;

import junit.framework.TestCase;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.trails.test.Foo;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class UtilsTest extends TestCase
{
	public void testUnqualify()
	{
		assertEquals("Foo", Utils.unqualify(Foo.class.getName()));
	}

	public void testPluralize()
	{
		assertEquals("keys", Utils.pluralize("key"));
		assertEquals("nouns", Utils.pluralize("noun"));
		assertEquals("words", Utils.pluralize("word"));
		assertEquals("properties", Utils.pluralize("property"));
		assertEquals("bosses", Utils.pluralize("boss"));
	}

	public void testCheckForCGLIB()
	{
		Enhancer enb = new Enhancer();
		enb.setUseCache(false);
		enb.setInterceptDuringConstruction(false);
		enb.setCallbackType(MethodInterceptor.class);
		enb.setSuperclass(Foo.class);

		Enhancer enbCB = new Enhancer();
		enbCB.setUseCache(false);
		enbCB.setInterceptDuringConstruction(false);
		enbCB.setCallbackType(MethodInterceptor.class);
		enbCB.setSuperclass(Foo.class);
		enbCB.setCallback(new MethodInterceptor()
		{
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
			{
				return proxy.invokeSuper(obj, args);
			}
		});


		assertEquals(Foo.class, Utils.checkForCGLIB(Foo.class));
		assertEquals(Foo.class, Utils.checkForCGLIB(enb.createClass()));
		assertEquals(Foo.class, Utils.checkForCGLIB(enbCB.create().getClass()));
	}

	public void testUnCamelCase()
	{
		assertEquals("Born On Date", Utils.unCamelCase("bornOnDate"));
		assertEquals("Born On Date", Utils.unCamelCase("born On Date"));
//		assertEquals("BOD", Utils.unCamelCase("BOD")); //@note: should this work?
	}
}
