package org.tynamo.services;

import java.util.Collection;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.BlockNotFoundException;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.slf4j.Logger;

public class SearchFilterBlockSourceImpl implements SearchFilterBlockSource
{
	// This is checked before masterSource
	private final SearchFilterBlockOverrideSource overrideSource;

	private final SearchFilterBlockOverrideSource masterSource;

	private Logger logger;

	public SearchFilterBlockSourceImpl(Logger logger, RequestPageCache pageCache,
		SearchFilterBlockOverrideSource overrideSource,
		Collection<SearchFilterBlockContribution> configuration) {
		this.logger = logger;
		this.overrideSource = overrideSource;
		masterSource = new SearchFilterBlockOverrideSourceImpl(pageCache, configuration);
	}

	@Override
	public Block toBlock(String datatype) {
		Block result = overrideSource.toBlock(datatype);

		try {
			if (result == null) result = masterSource.toBlock(datatype);
		} catch (BlockNotFoundException e) {
			// We don't want to have built-in search support for all data types, do we?
			logger.debug("No search filter block found for datatype " + datatype);
		}
		// if (result == null)
		// throw new RuntimeException(
		// String
		// .format(
		// "There is no defined way to set a search filter for data of type '%s'.  Make a contribution to the SearchFilterBlockSource service for this type.",
		// datatype));

		return result;
	}

}
