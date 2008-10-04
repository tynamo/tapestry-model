package org.trails.page;

/**
 * A page which has a model object.
 */
public interface IModelPage extends SimpleTrailsBasePage
{
	Object getModel();

	void setModel(Object model);

	boolean isModelNew();

	void setModelNew(boolean modelNew);
}
