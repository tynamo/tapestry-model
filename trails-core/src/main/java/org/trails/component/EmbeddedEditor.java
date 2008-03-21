package org.trails.component;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.trails.builder.BuilderDirector;
import org.trails.descriptor.EmbeddedDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

/**
 * This component displays an @Embedded object
 */
@ComponentClass
public abstract class EmbeddedEditor extends BaseComponent
{

	@InjectObject("service:trails.core.BuilderDirector")
	public abstract BuilderDirector getBuilderDirector();

	@Parameter(required = true)
	public abstract IPropertyDescriptor getDescriptor();

	@Parameter(required = true)
	public abstract Object getModel();

	public abstract void setModel(Object model);

	public EmbeddedDescriptor getEmbeddedDescriptor()
	{
		return (EmbeddedDescriptor) getDescriptor();
	}

	@Override
	protected void prepareForRender(IRequestCycle cycle)
	{
		/**
		 * fix for TRAILS-53
		 * In a proper implementation this code should be in a pageBeginRender(PageEvent event) method, but we need this
		 * code here because pageBeginRender isn't fired when the component is inside a Block in another page.
		 *
		 */
		if (!cycle.isRewinding() && getModel() == null)
		{
			setModel(getBuilderDirector().createNewInstance(getEmbeddedDescriptor().getPropertyType()));
		}
		super.prepareForRender(cycle);
	}
}
