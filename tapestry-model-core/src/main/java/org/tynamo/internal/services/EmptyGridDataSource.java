package org.tynamo.internal.services;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

public class EmptyGridDataSource implements GridDataSource {
	private Class beanType;

	public EmptyGridDataSource(Class beanType) {
		this.beanType = beanType;
	}

	@Override
	public int getAvailableRows() {
		return 0;
	}

	@Override
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
	}

	@Override
	public Object getRowValue(int index) {
		return null;
	}

	@Override
	public Class getRowType() {
		return beanType;
	}

}
