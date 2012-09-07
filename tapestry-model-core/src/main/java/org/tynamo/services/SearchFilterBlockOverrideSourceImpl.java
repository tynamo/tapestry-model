// Copyright 2008 The Apache Software Foundation
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

package org.tynamo.services;

import java.util.Collection;
import java.util.Map;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

public class SearchFilterBlockOverrideSourceImpl implements SearchFilterBlockOverrideSource
{
	private final RequestPageCache pageCache;

	private final Map<String, SearchFilterBlockContribution> searchFilterMap = CollectionFactory.newCaseInsensitiveMap();

	public SearchFilterBlockOverrideSourceImpl(RequestPageCache pageCache,
		Collection<SearchFilterBlockContribution> configuration) {
		this.pageCache = pageCache;

		for (SearchFilterBlockContribution contribution : configuration)
			searchFilterMap.put(contribution.getDataType(), contribution);
	}

	@Override
	public Block toBlock(String datatype) {
		SearchFilterBlockContribution contribution = searchFilterMap.get(datatype);
		if (contribution == null) return null;

		Page page = pageCache.get(contribution.getPageName());

		return page.getRootElement().getBlock(contribution.getBlockId());
	}
}
