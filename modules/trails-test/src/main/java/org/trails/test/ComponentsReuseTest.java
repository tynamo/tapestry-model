package org.trails.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.apache.tapestry.test.Creator;

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
}
