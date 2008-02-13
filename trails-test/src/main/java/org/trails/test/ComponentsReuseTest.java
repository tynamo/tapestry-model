package org.trails.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.apache.tapestry.test.Creator;
import ognl.Ognl;
import ognl.OgnlException;

public class ComponentsReuseTest extends TestCase
{

	protected Creator creator = new Creator();

	public void testComponentA()
	{
		ComponentA component = (ComponentA) creator.newInstance(ComponentA.class, new Object[]{"elements", new ArrayList()});
		assertNotNull(component);
		assertNotNull(component.getElements());
	}

	public void testComponentB()
	{
		ComponentB component = (ComponentB) creator.newInstance(ComponentB.class, new Object[]{"elements", new ArrayList()});
		assertNotNull(component);
		assertNotNull(component.getElements());
	}

	public void testComponentC()
	{
		try
		{
			ComponentA componentA =
					(ComponentA) creator.newInstance(ComponentA.class, new Object[]{"elements", new ArrayList()});
			assertNotNull(componentA);
			assertNotNull(Ognl.getValue("elements", componentA));

			ComponentC componentC =
					(ComponentC) creator.newInstance(ComponentC.class, new Object[]{"listElements", new ArrayList()});
			assertNotNull(componentC);
			assertNotNull(Ognl.getValue("elements", componentC));

		} catch (OgnlException e)
		{
			fail();
		}
	}
}
