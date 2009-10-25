package org.tynamo.blob;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.services.AssetFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * The Trails {@link org.trails.component.blob.IconResolver} default implementation.
 */
public class DefaultIconResolver implements IconResolver
{

	private AssetFactory classpathAssetFactory;

	public DefaultIconResolver(AssetFactory classpathAssetFactory)
	{
		this.classpathAssetFactory = classpathAssetFactory;
	}

	private static Map<String, String> map = new HashMap<String, String>();
	private Map<String, Asset> assetsMap = new HashMap<String, Asset>();

	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

	static
	{
		map.put("application/x-zip-compressed", "/org/trails/component/blob/image/asset/winzip.gif");
		map.put("application/pdf", "/org/trails/component/blob/image/asset/icadobe.gif");
		map.put("application/msword", "/org/trails/component/blob/image/asset/icdoc.gif");
		map.put("application/vnd.visio", "/org/trails/component/blob/image/asset/icdoc.gif");
		map.put("application/vnd.ms-powerpoint", "/org/trails/component/blob/image/asset/icppt.gif");
		map.put("application/vnd.ms-excel", "/org/trails/component/blob/image/asset/icxls.gif");
		map.put(DEFAULT_CONTENT_TYPE, "/org/trails/component/blob/image/asset/icgen.gif");
		map.put("text/html", "/org/trails/component/blob/image/asset/ichtm.gif");
		map.put("text/plain", "/org/trails/component/blob/image/asset/ictxt.gif");
		map.put("text/css", "/org/trails/component/blob/image/asset/ictxt.gif");
		map.put("text/xml", "/org/trails/component/blob/image/asset/icxml.gif");
		map.put("image/tiff", "/org/trails/component/blob/image/asset/icgen.gif");
		map.put("video/avi", "/org/trails/component/blob/image/asset/icwmp.gif");
		map.put("video/mpeg", "/org/trails/component/blob/image/asset/icwmp.gif");
		map.put("video/mp4", "/org/trails/component/blob/image/asset/icwmp.gif");
		map.put("video/quicktime", "/org/trails/component/blob/image/asset/icwmp.gif");
		map.put("video/x-ms-wmv", "/org/trails/component/blob/image/asset/icwmp.gif");
	}

	public Asset getAsset(String contentType)
	{
		if (!assetsMap.containsKey(contentType))
		{
			if (map.containsKey(contentType))
			{
				assetsMap.put(contentType,
						classpathAssetFactory.createAsset(new ClasspathResource(map.get(contentType))));
			} else
			{
				return getAsset(DEFAULT_CONTENT_TYPE);
			}
		}
		return assetsMap.get(contentType);
	}
}
