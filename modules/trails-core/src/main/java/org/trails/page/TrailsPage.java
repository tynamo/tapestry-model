/*
 * Created on Mar 15, 2005
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
package org.trails.page;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.html.BasePage;
import org.trails.callback.CallbackStack;
import org.trails.descriptor.DescriptorService;
import org.trails.i18n.TrailsMessageSource;
import org.trails.persistence.PersistenceService;

public abstract class TrailsPage extends BasePage implements ITrailsPage
{

	public void pushCallback()
	{

	}

	/**
	 * This property is injected with the callbackStack ASO
	 *
	 * @return
	 */
	@InjectState("callbackStack")
	public abstract CallbackStack getCallbackStack();

	/**
	 * This property is injected with the persistenceService bean
	 *
	 * @return
	 */
	@InjectObject("service:trails.core.PersistenceService")
	public abstract PersistenceService getPersistenceService();

	/**
	 * This property is injected with the descriptorService bean
	 *
	 * @return
	 */
	@InjectObject("service:trails.core.DescriptorService")
	public abstract DescriptorService getDescriptorService();

	/**
	 * Message source to i18n pages
	 *
	 * @return
	 */
	@InjectObject("service:trails.core.MessageSource")
	public abstract TrailsMessageSource getResourceBundleMessageSource();

	public void activateTrailsPage(Object[] objects, IRequestCycle iRequestCycle)
	{
		pushCallback();
	}

	@InjectObject(value = "service:trails.core.TrailsPagesService")
	public abstract IEngineService getTrailsPagesService();

}
