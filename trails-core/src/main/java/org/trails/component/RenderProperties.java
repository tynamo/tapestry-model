package org.trails.component;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.IPropertyDescriptor;

/**
 * This component loops over all the properties in the class descriptor and
 * displays a property for each one, deferring to a property override block if there is one.
 */
@ComponentClass
public abstract class RenderProperties extends AbstractObjectRenderComponent
{

	/**
	 * This property indicates where the HTML template for this component is located.
	 * It's explicitly indicated here so all the subclasses can share the same template.
	 *
	 * @return Returns the HTML template.
	 */
	@Asset(value = "/org/trails/component/RenderProperties.html")
	public abstract IAsset get$template();

	@Parameter
	public abstract BlockFinder getBlockFinder();

	@Parameter
	public abstract Object getModel();


	/**
	 * It's used by the @For component
	 *
	 * @return Returns the current property.
	 */
	public abstract IPropertyDescriptor getProperty();
}