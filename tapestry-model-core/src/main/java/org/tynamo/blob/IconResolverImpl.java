package org.tynamo.blob;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.ClasspathProvider;

import java.util.HashMap;
import java.util.Map;

public class IconResolverImpl implements IconResolver
{
	private final Map<String, Asset> assets;


	public IconResolverImpl(@ClasspathProvider AssetFactory classpathAssetFactory, Map<String, String> configuration)
	{
		this.assets = new HashMap<String, Asset>();
		for (Map.Entry<String, String> entry : configuration.entrySet())
		{
			assets.put(entry.getKey(), classpathAssetFactory.createAsset(new ClasspathResource(entry.getValue())));
		}
	}

	public Asset getAsset(String contentType)
	{
		return assets.get(contentType);
	}
}
