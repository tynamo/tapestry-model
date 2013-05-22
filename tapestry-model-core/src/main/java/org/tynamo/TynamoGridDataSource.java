package org.tynamo;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.tynamo.services.PersistenceService;

import java.util.List;

/**
 * A simple implementation of {@link org.apache.tapestry5.grid.GridDataSource} based on a Tynamo PersistenceService and a known
 * entity class.  This implementation does support multiple {@link org.apache.tapestry5.grid.SortConstraint sort
 * constraints}; however it assumes a direct mapping from sort constraint property to Hibernate property.
 * <p/>
 * This class is <em>not</em> thread-safe; it maintains internal state.
 * <p/>
 * Typically, an instance of this object is created fresh as needed (that is, it is not stored between requests).
 *
 * @deprecated user PersistenceService#getGridDataSource() instead
 *
 */
@Deprecated
public class TynamoGridDataSource implements GridDataSource
{

	private final PersistenceService persistenceService;

	private final Class entityType;

	private int startIndex;

	private List preparedResults;

	public TynamoGridDataSource(PersistenceService persistenceService, Class entityType)
	{
		this.persistenceService = persistenceService;
		this.entityType = entityType;
	}

	/**
	 * Returns the total number of rows for the configured entity type.
	 */
	public int getAvailableRows()
	{
		return persistenceService.count(entityType);
	}

	/**
	 * Prepares the results, performing a query (applying the sort results, and the provided start and end index). The
	 * results can later be obtained from {@link #getRowValue(int)} }.
	 *
	 * @param startIndex      index, from zero, of the first item to be retrieved
	 * @param endIndex        index, from zero, of the last item to be retrieved
	 * @param sortConstraints zero or more constraints used to set the order of the returned values
	 */
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints)
	{
		this.startIndex = startIndex;
		preparedResults = persistenceService.getInstances(entityType, startIndex, (endIndex - startIndex) + 1);
	}

	/**
	 * Returns a row value at the given index (which must be within the range defined by the call to {@link
	 * #prepare(int, int, java.util.List)} ).
	 *
	 * @param index of object
	 * @return object at that index
	 */
	public Object getRowValue(int index)
	{
		return preparedResults.get(index - startIndex);
	}

	/**
	 * Returns the entity type, as provided via the constructor.
	 */
	public Class getRowType()
	{
		return entityType;
	}
}
