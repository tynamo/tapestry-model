package org.trailsframework;

import java.io.IOException;
import java.util.Properties;

public abstract class VersionedModule
{
	protected static final String version;

	static
	{
		Properties moduleProperties = new Properties();
		String aVersion = "unversioned";
		try
		{
			moduleProperties.load(VersionedModule.class.getResourceAsStream("module.properties"));
			aVersion = moduleProperties.getProperty("module.version");
		} catch (IOException e)
		{
			// ignore
		}
		version = aVersion;
	}

}
