package org.tynamo.services;

import org.apache.tapestry5.beanmodel.BeanModel;

public interface BeanModelModifier
{

	boolean modify(BeanModel<?> beanModel, String key);

}
