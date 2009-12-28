package org.tynamo.builder;

/**
 * Fulfils the "Builder" role in the Tynamo implementation of
 * GOF's <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 *
 * Specifies an abstract interface for creating entities.
 */
public interface Builder<T>
{
	T build();
}
