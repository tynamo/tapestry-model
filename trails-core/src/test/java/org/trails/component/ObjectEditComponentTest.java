/*
 * Created on Jan 11, 2005
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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.valid.IValidationDelegate;
import org.jmock.cglib.Mock;
import org.trails.callback.CallbackStack;
import org.trails.descriptor.DescriptorService;
import org.trails.i18n.ResourceBundleMessageSource;
import org.trails.page.EditPage;
import org.trails.persistence.PersistenceService;
import org.trails.test.Foo;
import org.trails.validation.TrailsValidationDelegate;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectEditComponentTest extends ComponentTest
{


	Mock pageMock;
	ObjectEditComponent editComponent;
	Mock cycleMock;
	HashMap thisPageComponents = new HashMap();
	HashMap fooComponents = new HashMap();
	IRequestCycle cycle;
	MockFooEditPage fooEditPage;

	public void testHasBlock()
	{


		thisPageComponents.put("blah2", creator.newInstance(Block.class));

		fooComponents.put("blah", creator.newInstance(Block.class));

		//assertTrue("found our block", editComponent.hasBlock("blah"));
		assertFalse("one thats not there", editComponent.hasBlock("qwerqrew"));

		assertTrue("found block on this page", editComponent.hasBlock("blah2"));
	}

	public void setUp()
	{
		editComponent = (ObjectEditComponent) creator.newInstance(ObjectEditComponent.class);
		editComponent.setModel(new Foo());
		fooEditPage = new MockFooEditPage();
		fooEditPage.setComponents(fooComponents);
		// the page we are on
		pageMock = new Mock(IPage.class);

		editComponent.setPage((IPage) pageMock.proxy());
		// the page for the object

		cycleMock = new Mock(IRequestCycle.class);
		cycle = (IRequestCycle) cycleMock.proxy();

		pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(thisPageComponents));

		//pageMock.expects(atLeastOnce()).method("getRequestCycle").will(returnValue(cycle));
		//cycleMock.expects(atLeastOnce()).method("getPage").with(eq("FooEdit")).will(returnValue(fooEditPage));
	}

	public void testGetBlock() throws Exception
	{
		Block block1 = (Block) creator.newInstance(Block.class);
		Block block2 = (Block) creator.newInstance(Block.class);
		Block otherblock = (Block) creator.newInstance(Block.class);
		thisPageComponents.put("block", block2);
//        fooComponents.put("block", block1);
//        fooComponents.put("otherblock", otherblock);

		//pageMock2.expects(atLeastOnce()).method("getComponent").with(eq("block")).will(returnValue(block1));
		pageMock.expects(atLeastOnce()).method("getComponent").with(eq("block")).will(returnValue(block2));
		//pageMock.expects(atLeastOnce()).method("getComponent").with(eq("otherblock")).will(returnValue(null));

		assertEquals("right block", block2, editComponent.getBlock("block"));

//        assertEquals("right block", otherblock, editComponent.getBlock("otherblock"));
//        assertNotNull("model is passed", fooEditPage.getModel());

	}

	public class MockFooEditPage extends EditPage
	{
		private Object model;

		private CallbackStack callbackStack = new CallbackStack();

		private TrailsValidationDelegate delegate = new TrailsValidationDelegate();

		private Map components;

		/* (non-Javadoc)
				 * @see org.trails.page.EditPage#getPersistenceService()
				 */
		public PersistenceService getPersistenceService()
		{
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
				 * @see org.trails.page.EditPage#getPropertyDescriptorService()
				 */
		public DescriptorService getDescriptorService()
		{
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
				 * @see org.trails.page.EditPage#setDelegate(org.apache.tapestry.valid.IValidationDelegate)
				 */
		public void setDelegate(IValidationDelegate Delegate)
		{
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
				 * @see org.trails.page.EditPage#setPersistenceService(org.trails.persistence.PersistenceService)
				 */
		public void setPersistenceService(PersistenceService psvc)
		{
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
				 * @see org.trails.page.EditPage#setPropertyDescriptorService(org.trails.descriptor.PropertyDescriptorService)
				 */
		public void setDescriptorService(
			DescriptorService PropertyDescriptorService)
		{
			// TODO Auto-generated method stub

		}

		/**
		 * @return Returns the model.
		 */
		public Object getModel()
		{
			return model;
		}

		/**
		 * @param model The model to set.
		 */
		public void setModel(Object model)
		{
			this.model = model;
		}

		/**
		 * @return Returns the components.
		 */
		public Map getComponents()
		{
			return components;
		}

		/**
		 * @param components The components to set.
		 */
		public void setComponents(Map components)
		{
			this.components = components;
		}


		/* (non-Javadoc)
				 * @see org.apache.tapestry.IComponent#getComponent(java.lang.String)
				 */
		public IComponent getComponent(String key)
		{
			// TODO Auto-generated method stub
			return (IComponent) components.get(key);
		}

		/* (non-Javadoc)
				 * @see org.trails.page.EditPage#getNextPage()
				 */
		public ICallback getNextPage()
		{
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
				 * @see org.trails.page.EditPage#setNextPage(org.apache.tapestry.callback.ICallback)
				 */
		public void setNextPage(ICallback NextPage)
		{
			// TODO Auto-generated method stub

		}

		public TrailsValidationDelegate getDelegate()
		{
			return delegate;
		}

		public void setDelegate(TrailsValidationDelegate delegate)
		{
			this.delegate = delegate;
		}

		public CallbackStack getCallbackStack()
		{
			return callbackStack;
		}

		public void setCallbackStack(CallbackStack callbackStack)
		{
			this.callbackStack = callbackStack;
		}

		@Override
		public ResourceBundleMessageSource getResourceBundleMessageSource()
		{
			// TODO Auto-generated method stub
			return null;
		}

	}
}
