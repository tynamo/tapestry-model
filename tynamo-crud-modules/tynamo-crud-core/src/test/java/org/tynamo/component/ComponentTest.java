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
package org.tynamo.component;


import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.MessageFinderImpl;
import org.apache.hivemind.impl.ModuleMessages;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.hivemind.service.impl.ThreadLocaleImpl;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.engine.IEngineService;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.tynamo.callback.CallbackStack;
import org.tynamo.descriptor.DescriptorService;
import org.tynamo.i18n.DescriptorInternationalization;
import org.tynamo.i18n.HiveMindMessageSource;
import org.tynamo.i18n.TrailsMessageSource;
import org.tynamo.page.EditPage;
import org.tynamo.persistence.PersistenceService;
import org.tynamo.validation.TrailsValidationDelegate;

import java.util.Locale;

public class ComponentTest extends MockObjectTestCase
{
	protected Creator creator = new Creator();
	protected Mock descriptorServiceMock = new Mock(DescriptorService.class);
	protected DescriptorService descriptorService;
	protected CallbackStack callbackStack = new CallbackStack();
	protected Mock persistenceMock = new Mock(PersistenceService.class);
	protected TrailsValidationDelegate delegate = new TrailsValidationDelegate();
	protected ThreadLocale threadLocale = 	new ThreadLocaleImpl(Locale.ENGLISH);
	protected Mock pageServiceMock = new Mock(IEngineService.class);

	protected void setUp() throws Exception
	{
		descriptorService = (DescriptorService) descriptorServiceMock.proxy();
		threadLocale.setLocale(Locale.ENGLISH);

		/** sometime the aspect weaves classes that it shouldn't weave **/
		DescriptorInternationalization.aspectOf().setTrailsMessageSource(null);
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
		TrailsMessageSource messageSource = getNewTrailsMessageSource();

		EditPage editPage = (EditPage) creator.newInstance(EditPage.class,
			new Object[]{
				"persistenceService", persistenceMock.proxy(),
				"descriptorService", descriptorServiceMock.proxy(),
				"callbackStack", callbackStack,
				"delegate", delegate,
				"resourceBundleMessageSource", messageSource,
				"trailsPagesService", pageServiceMock.proxy()
			});
		return editPage;
	}

	public TrailsMessageSource getNewTrailsMessageSource() {
		HiveMindMessageSource messageSource = new HiveMindMessageSource();
		Messages hivemindMessages =
			new ModuleMessages(new MessageFinderImpl(new ClasspathResource(new DefaultClassResolver(),"messagestest.properties")), threadLocale);
		messageSource.setMessageSource(hivemindMessages);
		return  messageSource;
	}

	protected void tearDown() throws Exception
	{
		threadLocale.setLocale(Locale.ENGLISH);
		/** sometime the aspect weaves classes that it shouldn't weave **/
		DescriptorInternationalization.aspectOf().setTrailsMessageSource(null);
	}
}
