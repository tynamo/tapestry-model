package org.trails.link;


import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.AbstractLinkComponent;
import org.apache.tapestry.link.ILinkRenderer;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.engine.TrailsPagesServiceParameter;
import org.trails.page.PageType;

/**
 * A component for creating a link to {@link org.trails.page.TrailsPage} using
 * the {@link org.trails.engine.TrailsPagesService}.
 */

@ComponentClass
public abstract class TrailsLink extends AbstractLinkComponent
{

	@InjectObject(value = "service:trails.core.TrailsPagesService")
	public abstract IEngineService getTrailsPagesService();

	public ILink getLink(IRequestCycle cycle)
	{
		TrailsPagesServiceParameter esp = new TrailsPagesServiceParameter(getPageType(), getClassDescriptor(), getModel(), getAssociationDescriptor(), getParent());
		return getTrailsPagesService().getLink(false, esp);
	}

	@Parameter(required = true)
	public abstract PageType getPageType();

	@Parameter(required = true)
	public abstract IClassDescriptor getClassDescriptor();

	@Parameter
	public abstract Object getModel();

	@Parameter
	public abstract CollectionDescriptor getAssociationDescriptor();

	@Parameter
	public abstract Object getParent();

	@Parameter
	public abstract boolean isDisabled();

	@Parameter
	public abstract ILinkRenderer getRenderer();

	@Parameter
	public abstract String getAnchor();

	/**
	 * Forces the link to be generated as an absolute URL with the given scheme
	 * (unless the scheme matches the scheme for the current request).
	 *
	 * @return
	 */
	@Parameter
	public abstract String getScheme();

	/**
	 * Forces the link to be generated as an absolute URL with the given port
	 * (unless the port matches the port for the current request).
	 *
	 * @return
	 */
	@Parameter
	public abstract Integer getPort();

	@Parameter
	public abstract String getTarget();

}
