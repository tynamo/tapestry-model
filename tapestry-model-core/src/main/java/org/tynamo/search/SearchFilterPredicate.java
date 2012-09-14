package org.tynamo.search;


public class SearchFilterPredicate {
	private SearchFilterOperator operator;
	private Object lowValue;
	private Object highValue;

	public SearchFilterPredicate() {
		this(SearchFilterOperator.any, null, null);
	}

	public SearchFilterPredicate(SearchFilterOperator operator, Object lowValue, Object highValue) {
		this.operator = operator;
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	public void setOperator(SearchFilterOperator operator) {
		this.operator = operator;
	}

	public void setLowValue(Object lowValue) {
		this.lowValue = lowValue;
	}

	public void setHighValue(Object highValue) {
		this.highValue = highValue;
	}

	public SearchFilterOperator getOperator() {
		return operator;
	}

	public Object getLowValue() {
		return lowValue;
	}

	public Object getHighValue() {
		return highValue;
	}

}
