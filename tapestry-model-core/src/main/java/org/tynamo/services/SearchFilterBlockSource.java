package org.tynamo.services;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ioc.annotations.UsesConfiguration;
import org.apache.tapestry5.services.BeanBlockContribution;

/**
 * A source of {@link Block}s used to display the properties of a bean (used by the {@link
 * org.apache.tapestry5.corelib.components.Grid} component), or to edit the properties of a bean (used by the {@link
 * org.apache.tapestry5.corelib.components.BeanEditForm} component). Contributions to this service (a configuration of
 * {@link BeanBlockContribution}s) define what properties may be editted.
 * <p/>
 * Blocks are accessed in terms of a <strong>data type</strong> a string that identifies the type of data to be editted,
 * such as "string", "date", "boolean", etc.
 * <p/>
 * Tapestry contributes a number of default data types and corresponding edit and display blocks. The {@link
 * org.apache.tapestry5.services.BeanBlockOverrideSource} service allows these to be overridden.
 *
 * @see org.apache.tapestry5.services.DataTypeAnalyzer
 * @see org.apache.tapestry5.services.TapestryModule#provideDefaultBeanBlocks(org.apache.tapestry5.ioc.Configuration)
 */
@UsesConfiguration(SearchFilterBlockContribution.class)
public interface SearchFilterBlockSource
{
    /**
     * Returns a block which can be used to render an editor for the given data type, in the form of a field label and
     * input field.
     *
     * @param datatype logical name for the type of data to be displayed
     * @return the Block
     * @throws RuntimeException if no appropriate block is available
     */
	Block toBlock(String datatype);

}
