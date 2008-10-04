package org.trailsframework.builder;

/**
 * Fulfils the "Builder" role in the Trails implementation of
 * GOF's <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 *
 * Specifies an abstract interface for creating entities.
 */
public interface Builder<T>
{
	T build();
}
