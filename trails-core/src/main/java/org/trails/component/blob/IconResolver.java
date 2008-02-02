package org.trails.component.blob;

import org.apache.tapestry.IAsset;

public interface IconResolver
{
	IAsset getAsset(String contentType);
}
