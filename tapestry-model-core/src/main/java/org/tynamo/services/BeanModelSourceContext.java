package org.tynamo.services;

/**
 * Holds the context key used to decorate the BeanModels in {@link org.tynamo.internal.services.BeanModelSourceAdviceImpl}
 */
public class BeanModelSourceContext
{

	private String key;

	public BeanModelSourceContext(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}
}
