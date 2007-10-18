package org.trails.component;

import org.apache.tapestry.annotations.InjectObject;
import org.trails.descriptor.BlockFinder;

public abstract class ViewProperties extends RenderProperties
{
	@InjectObject("spring:viewerService")
	public abstract BlockFinder getBlockFinder();
}