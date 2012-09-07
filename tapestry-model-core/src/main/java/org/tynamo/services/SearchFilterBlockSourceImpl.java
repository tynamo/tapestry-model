package org.tynamo.services;

import java.util.Collection;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.internal.services.RequestPageCache;

public class SearchFilterBlockSourceImpl implements SearchFilterBlockSource
{
	// This is checked before masterSource
	private final SearchFilterBlockOverrideSource overrideSource;

	private final SearchFilterBlockOverrideSource masterSource;

	public SearchFilterBlockSourceImpl(RequestPageCache pageCache, SearchFilterBlockOverrideSource overrideSource,
		Collection<SearchFilterBlockContribution> configuration) {
		this.overrideSource = overrideSource;
		masterSource = new SearchFilterBlockOverrideSourceImpl(pageCache, configuration);
	}

	@Override
	public Block toBlock(String datatype) {
		Block result = overrideSource.toBlock(datatype);

		if (result == null) result = masterSource.toBlock(datatype);

		if (result == null)
			throw new RuntimeException(
				String
					.format(
						"There is no defined way to set a search filter for data of type '%s'.  Make a contribution to the SearchFilterBlockSource service for this type.",
						datatype));

		return result;
	}

}
