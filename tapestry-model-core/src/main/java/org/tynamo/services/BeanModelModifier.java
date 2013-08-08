package org.tynamo.services;

import org.apache.tapestry5.beaneditor.BeanModel;

public interface BeanModelModifier
{

	boolean modify(BeanModel<?> beanModel, String key);

}
