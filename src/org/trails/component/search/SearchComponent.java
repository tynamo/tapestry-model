package org.trails.component.search;

import org.hibernate.criterion.Criterion;

public interface SearchComponent
{
	public Criterion buildCriterion();
}
