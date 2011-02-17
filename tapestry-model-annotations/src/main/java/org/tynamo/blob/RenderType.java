package org.tynamo.blob;

public enum RenderType
{
	IMAGE, LINK; //, IFRAME, ICON; not yet supported in Tynamo

	public boolean isImage()
	{
		return this == IMAGE;
	}

	public boolean isLink()
	{
		return this == LINK;
	}

	public boolean isIFrame()
	{
		return false; // this == IFRAME;
	}

	public boolean isIcon()
	{
		return false; // this == ICON;
	}
}
