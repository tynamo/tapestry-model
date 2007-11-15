package org.trails.page;

import org.apache.tapestry.IPage;

/**
 * A page which has a model object.
 */
public interface IModelPage extends IPage
{
	Object getModel();

	void setModel(Object model);

	boolean isModelNew();

	void setModelNew(boolean modelNew);
}
