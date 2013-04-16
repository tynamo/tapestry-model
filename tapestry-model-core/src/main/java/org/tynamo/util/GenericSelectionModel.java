package org.tynamo.util;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.util.AbstractSelectModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GenericSelectionModel<T> extends AbstractSelectModel
{

	private final String labelField;
	private final List<T> list;
	private final PropertyAccess adapter;
	private final boolean sortByLabel;

	public GenericSelectionModel(List<T> list)
	{
		this(list, null, null, true);
	}

	public GenericSelectionModel(List<T> list, boolean sortByLabel)
	{
		this(list, null, null, sortByLabel);
	}


	public GenericSelectionModel(List<T> list, String labelField, PropertyAccess adapter, boolean sortByLabel)
	{
		this.labelField = labelField;
		this.list = list;
		this.adapter = adapter;
		this.sortByLabel = sortByLabel;
	}

	public List<OptionGroupModel> getOptionGroups()
	{
		return Collections.emptyList();
	}

	public List<OptionModel> getOptions()
	{
		List<OptionModel> optionModelList = new ArrayList<OptionModel>();
		for (T obj : list)
		{
			if (labelField == null)
			{
				optionModelList.add(new OptionModelImpl(obj.toString(), obj));
			} else
			{
				optionModelList.add(new OptionModelImpl(adapter.get(obj, labelField).toString(), obj));
			}
		}

		if (sortByLabel)
		{
			Collections.sort(optionModelList, new Comparator<OptionModel>()
			{
				public int compare(OptionModel o1, OptionModel o2)
				{
					return o1.getLabel().compareTo(o2.getLabel());
				}
			});
		}

		return optionModelList;
	}
}