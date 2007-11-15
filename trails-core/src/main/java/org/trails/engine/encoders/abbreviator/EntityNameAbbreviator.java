package org.trails.engine.encoders.abbreviator;

public interface EntityNameAbbreviator
{

	public String abbreviate(Class clazz);

	public Class unabbreviate(String abbreviation);
}
