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


import org.apache.tapestry.test.Creator;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.callback.CallbackStack;
import org.trails.descriptor.DescriptorService;
import org.trails.i18n.DefaultTrailsResourceBundleMessageSource;
import org.trails.page.EditPage;
import org.trails.persistence.PersistenceService;
import org.trails.validation.TrailsValidationDelegate;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ComponentTest extends MockObjectTestCase
{
	protected Creator creator = new Creator();
	protected Mock descriptorServiceMock = new Mock(DescriptorService.class);
	protected DescriptorService descriptorService;
	protected CallbackStack callbackStack = new CallbackStack();
	protected Mock persistenceMock = new Mock(PersistenceService.class);
	protected TrailsValidationDelegate delegate = new TrailsValidationDelegate();

	public void setUp() throws Exception
	{
		descriptorService = (DescriptorService) descriptorServiceMock.proxy();
	}

	public <T> T buildTrailsPage(Class<T> pageClass)
	{
		T page = (T) creator.newInstance(pageClass,
			new Object[]{
				"persistenceService", persistenceMock.proxy(),
				"descriptorService", descriptorServiceMock.proxy(),
				"callbackStack", callbackStack
			});
		return page;
	}

	protected EditPage buildEditPage()
	{
		DescriptorService descriptorService = (DescriptorService) descriptorServiceMock.proxy();
		DefaultTrailsResourceBundleMessageSource messageSource = new DefaultTrailsResourceBundleMessageSource();
		ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
		springMessageSource.setBasename("messages");
		messageSource.setMessageSource(springMessageSource);

		EditPage editPage = (EditPage) creator.newInstance(EditPage.class,
			new Object[]{
				"persistenceService", persistenceMock.proxy(),
				"descriptorService", descriptorServiceMock.proxy(),
				"callbackStack", callbackStack,
				"delegate", delegate,
				"resourceBundleMessageSource", messageSource
			});
		return editPage;
	}

}
