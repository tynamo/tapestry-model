package org.trails.engine.encoders.abbreviator;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.trails.component.Utils;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;

/**
 * This class is a Trails adaptation of Tapernate's DefaultAbbreviator
 * <p/>
 * Credits to James Carman.
 */
public class DefaultAbbreviator implements EntityNameAbbreviator
{

	private BidiMap abbreviationMap;
	private DescriptorService descriptorService;

	public String abbreviate(Class entityName)
	{
		return (String) abbreviationMap.get(entityName.getName());
	}

	public Class unabbreviate(String abbreviation)
	{
		return Utils.classForName((String) abbreviationMap.getKey(abbreviation));
	}

	public void initializeService()
	{
		abbreviationMap = new TreeBidiMap();
		for (IClassDescriptor classDescriptor : descriptorService.getAllDescriptors())
		{
			abbreviationMap.put(classDescriptor.getType().getName(), classDescriptor.getType().getSimpleName());
		}
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}
}
