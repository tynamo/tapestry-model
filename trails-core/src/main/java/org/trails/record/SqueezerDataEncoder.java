package org.trails.record;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry.record.PersistentPropertyDataEncoder;
import org.apache.tapestry.services.DataSqueezer;


/**
 * Responsible for squeezing lists of {@link org.apache.tapestry.record.PropertyChange}s back and
 * forth to a URL safe encoded string.
 */
public class SqueezerDataEncoder implements PersistentPropertyDataEncoder
{

	DataSqueezer dataSqueezer;

	public void setDataSqueezer(DataSqueezer dataSqueezer)
	{
		this.dataSqueezer = dataSqueezer;
	}

	public String encodePageChanges(List list)
	{
		Object[] objects = new Object[list.size()];
		objects = list.toArray(objects);

		String[] results = dataSqueezer.squeeze(objects);

		return arrayToString(results);
	}

	public List decodePageChanges(String s)
	{
		String[] strings = s.trim().substring(1, s.length() - 1).split(",");
		Object[] objects = dataSqueezer.unsqueeze(strings);
		return Arrays.asList(objects);
	}

	public String arrayToString(Object[] a)
	{
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "{}";

		StringBuilder b = new StringBuilder();
		b.append('{');
		for (int i = 0; ; i++)
		{
			b.append(String.valueOf(a[i]));
			if (i == iMax)
				return b.append('}').toString();
			b.append(",");
		}
	}
}
