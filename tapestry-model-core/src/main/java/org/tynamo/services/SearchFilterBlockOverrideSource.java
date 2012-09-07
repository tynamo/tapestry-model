package org.tynamo.services;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ioc.annotations.UsesConfiguration;

/**
 * Used to override the default {@link org.apache.tapestry5.services.BeanBlockSource} for a particular data type.  The
 * service accepts the same configuration of {@link org.apache.tapestry5.services.BeanBlockContribution}s as the main
 * service.
 */
@UsesConfiguration(SearchFilterBlockContribution.class)
public interface SearchFilterBlockOverrideSource
{
    /**
     * Returns a block which can be used to render an editor for the given data type, in the form of a field label and
     * input field.
     *
     * @param datatype logical name for the type of data to be displayed
     * @return the Block
     * @throws null if no override is available
     */
	Block toBlock(String datatype);
}
