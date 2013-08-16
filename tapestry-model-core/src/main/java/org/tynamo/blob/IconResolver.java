package org.tynamo.blob;

import org.apache.tapestry5.Asset;


/**
 * An interface to allow the {@link org.tynamo.components.Download} component to resolve icons in a custom fashion.
 */
public interface IconResolver
{
	/**
	 * Returns an Asset object for the given MIME type. It will be used as an icon for the download link.
	 *
	 * @param contentType the MIME type
	 * @return the icon asset for the specified MIME type
	 */
	Asset getAsset(String contentType);
}
