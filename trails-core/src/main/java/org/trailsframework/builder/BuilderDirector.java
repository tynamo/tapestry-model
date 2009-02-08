package org.trailsframework.builder;

import org.trailsframework.exception.TrailsRuntimeException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Fulfils the "Director" role in the Trails implementation of
 * GOF's <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 *
 * Constructs an object using the Builder interface 
 */
public class BuilderDirector
{

	Map<Class, Builder> builders;

	public BuilderDirector()
	{
		builders = new HashMap<Class, Builder>();
	}

	public BuilderDirector(Map<Class, Builder> builders)
	{
		this.builders = builders;
	}

	/**
	 * Create a new instance of an object of class 'type' using a Builder.
	 *
	 * @param type is a class whose instance should be created
	 * @return a newly created object
	 */
	public <T> T createNewInstance(final Class<T> type)
	{
		Builder<T> builder = (Builder<T>) builders.get(type);
		if (builder != null)
		{
			return builder.build();
		} else
		{
			return createNewInstanceFromEmptyConstructor(type);
		}
	}

	/**
	 * Create a new instance of an object of class 'type' using an empty constructor.
	 *
	 * @param type is a class whose instance should be created
	 * @return a newly created object
	 */
	private <T> T createNewInstanceFromEmptyConstructor(final Class<T> type)
	{
		try
		{
			Constructor constructor = type.getDeclaredConstructor();
			constructor.setAccessible(true);
			return (T) constructor.newInstance();

		} catch (Exception ex)
		{
			throw new TrailsRuntimeException(ex, type);
		}
	}
}
