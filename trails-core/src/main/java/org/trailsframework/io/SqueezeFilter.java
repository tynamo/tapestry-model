package org.trails.io;

import org.apache.tapestry.services.DataSqueezer;


/**
 * Interface which, as the SqueezeAdaptor, defines a class used to convert data for a specific Java type into a String
 * format (squeeze it), or convert from a String back into a Java type (unsqueeze).
 * This interface is used to turn the squeezer service (tapestry.data.DataSqueezer) into a pipeline.
 *
 * @author James Carman
 * @see org.apache.tapestry.util.io.SqueezeAdaptor
 */
public interface SqueezeFilter
{
	String[] squeeze(Object[] objects, DataSqueezer next);

	String squeeze(Object object, DataSqueezer next);

	Object[] unsqueeze(String[] strings, DataSqueezer next);

	Object unsqueeze(String string, DataSqueezer next);
}
