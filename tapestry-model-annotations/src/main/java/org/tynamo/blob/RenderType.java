package org.tynamo.blob;

public enum RenderType
{
	IMAGE, LINK, ICON; //, IFRAME not yet supported in Tynamo

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
		return this == ICON;
	}
}
