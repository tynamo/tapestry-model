package org.trailsframework.util;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.util.AbstractSelectModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class GenericSelectionModel<T> extends AbstractSelectModel
{

	private final String labelField;

	private final List<T> list;

	private final PropertyAccess adapter;

	public GenericSelectionModel(List<T> list)
	{
		this.labelField = null;
		this.list = list;
		this.adapter = null;
	}

	public GenericSelectionModel(List<T> list, String labelField, PropertyAccess adapter)
	{
		this.labelField = labelField;
		this.list = list;
		this.adapter = adapter;
	}

	public List<OptionGroupModel> getOptionGroups()
	{
		return Collections.EMPTY_LIST;
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
		return optionModelList;
	}
}