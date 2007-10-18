package org.trails.component;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.trails.page.PageType;

/**
 * This component displays a link to the EditPage for an object
 */
@ComponentClass
public abstract class AbstractModelNavigationLink extends Link
{

	/**
	 * This property indicates where the HTML template for this component is located.
	 * It's explicitly indicated here so all the subclasses can share the same template.
	 *
	 * @return Returns the HTML template.
	 */
	@Asset(value = "/org/trails/component/AbstractModelNavigationLink.html")
	public abstract IAsset get$template();

	@Parameter(required = true)
	public abstract PageType getPageType();

	@Parameter(required = true)
	public abstract Object getModel();

	public abstract void setModel(Object model);

	public String getModelPageName()
	{
		return getPageResolver().resolvePage(getPage().getRequestCycle(), Utils.checkForCGLIB(getModel().getClass()), getPageType()).getPageName();
	}
}