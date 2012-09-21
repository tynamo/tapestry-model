package org.tynamo.search;


public class SearchFilterPredicate {
	private boolean enabled;
	private SearchFilterOperator operator = SearchFilterOperator.eq;
	private Object lowValue;
	private Object highValue;

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
