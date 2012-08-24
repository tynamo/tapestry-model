package org.tynamo.services;

import org.apache.tapestry5.beaneditor.DataType;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.util.Pair;

import java.util.List;

public class TynamoDataTypeAnalyzer implements DataTypeAnalyzer
{

	private final DescriptorService descriptorService;
	private final List<Pair> editorMap;

	/**
	 *
 	 * @param descriptorService
	 * @param editorMap A list of pairs Pair<Predicate<TynamoPropertyDescriptor>, String> where the Predicate us ...
	 * and the String is the data type identifier used to select editor (or display) blocks
	 */
	public TynamoDataTypeAnalyzer(final DescriptorService descriptorService, final List<Pair> editorMap)
	{
		this.descriptorService = descriptorService;
		this.editorMap = editorMap;
	}

	/**
	 * The first data type identifier in the editorMap whose "key" evaluates to true for the descriptor will be used
	 * to load an editor (or display) block for the descriptor. Returns null if no match is found.
	 *
	 * @return returns the data type identifier, if known, or returns null if not known.
	 */
	public String identifyDataType(PropertyAdapter adapter)
	{
		if (adapter.getAnnotation(DataType.class) == null)
		{
			TynamoPropertyDescriptor propertyDescriptor = getPropertyDescriptor(adapter);
			if (propertyDescriptor != null) //ignore excluded properties
			{
				for (Pair<Predicate<TynamoPropertyDescriptor>, String> entry : editorMap)
				{
					if (entry.getKey().accept(propertyDescriptor))
					{
						return entry.getValue();
					}
				}
			}
		}

		// To avoid "no strategy" exceptions, we expect a contribution of Object.class to the empty
		// string. We convert that back to a null.
		return null;
	}

	private TynamoPropertyDescriptor getPropertyDescriptor(PropertyAdapter adapter)
	{
		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(adapter.getBeanType());

		if (classDescriptor != null)
		{
			return classDescriptor.getPropertyDescriptor(adapter.getName());
		}

		return null;
	}

}

