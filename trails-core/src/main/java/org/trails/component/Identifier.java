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

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Lifecycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.NumberValidator;
import org.trails.descriptor.IIdentifierDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.page.ModelPage;

/**
 * Displays an id property
 */
@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class Identifier extends BaseComponent
{
	@Parameter(defaultValue = "container.model")
	public abstract Object getModel();

	@Parameter(defaultValue = "container.modelNew")
	public abstract boolean isModelNew();

	@Parameter(required = true, cache = true)
	public abstract IPropertyDescriptor getDescriptor();

	@Parameter(required = false, defaultValue = "page.validatorTranslatorService.getValidator(descriptor)")
	public abstract IValidator getValidator();

	@Bean(lifecycle = Lifecycle.PAGE)
	public abstract NumberValidator getNumberValidator();

	public boolean isEditable()
	{
		return !((IIdentifierDescriptor) getDescriptor()).isGenerated() && ((ModelPage) getPage()).isModelNew();
	}
}
