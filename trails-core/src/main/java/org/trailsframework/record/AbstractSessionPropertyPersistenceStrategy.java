// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.trails.record;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.record.PropertyChange;
import org.apache.tapestry.record.PropertyPersistenceStrategy;
import org.apache.tapestry.record.RecordUtils;
import org.apache.tapestry.record.WebSessionAttributeCallback;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A basic {@link org.apache.tapestry.record.PropertyPersistenceStrategy},
 * which stores properties in the HttpSession as attributes. This is a clone of
 * {@link org.apache.tapestry.record.SessionPropertyPersistenceStrategy} with one difference, it allows you to specify
 * a different strategyId
 */
public abstract class AbstractSessionPropertyPersistenceStrategy implements PropertyPersistenceStrategy
{
	private static final Log LOG = LogFactory.getLog(AbstractSessionPropertyPersistenceStrategy.class);

	private String applicationId;
	private WebRequest request;

	protected abstract String getStrategyId();

	// Really, the name of the servlet; used as a prefix on all
	// HttpSessionAttribute keys
	// to keep things straight if multiple Tapestry apps are deployed
	// in the same WAR.

	public void store(String pageName, String idPath, String propertyName, Object newValue)
	{
		Defense.notNull(pageName, "pageName");
		Defense.notNull(propertyName, "propertyName");
		WebSession session = request.getSession(true);
		String attributeName = RecordUtils.buildChangeKey(getStrategyId(), applicationId, pageName, idPath, propertyName);
		session.setAttribute(attributeName, newValue);
	}

	public Collection getStoredChanges(String pageName)
	{
		Defense.notNull(pageName, "pageName");

		WebSession session = request.getSession(false);

		if (session == null)
			return Collections.EMPTY_LIST;

		final Collection result = new ArrayList();

		WebSessionAttributeCallback callback = new WebSessionAttributeCallback()
		{
			public void handleAttribute(WebSession sess, String name)
			{
				PropertyChange change = RecordUtils.buildChange(name, sess.getAttribute(name));

				result.add(change);
			}
		};

		RecordUtils.iterateOverMatchingAttributes(getStrategyId(), applicationId, pageName, session, callback);

		return result;
	}

	public void discardStoredChanges(String pageName)
	{
		WebSession session = request.getSession(false);

		if (session == null) return;

		WebSessionAttributeCallback callback = new WebSessionAttributeCallback()
		{
			public void handleAttribute(WebSession sess, String name)
			{
				sess.setAttribute(name, null);
			}
		};

		RecordUtils.iterateOverMatchingAttributes(getStrategyId(), applicationId, pageName, session, callback);
	}

	/**
	 * Does nothing; session persistence does not make use of query parameters.
	 */

	public void addParametersForPersistentProperties(ServiceEncoding encoding,
													 boolean post)
	{
	}

	public void setApplicationId(String applicationName)
	{
		this.applicationId = applicationName;
	}

	public void setRequest(WebRequest request)
	{
		this.request = request;
	}
}
