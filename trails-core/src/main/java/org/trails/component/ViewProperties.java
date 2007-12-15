package org.trails.component;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.ComponentClass;
import org.trails.finder.BlockFinder;

@ComponentClass
public abstract class ViewProperties extends RenderProperties
{
	@InjectObject("service:trails.core.ViewerService")
	public abstract BlockFinder getBlockFinder();
}