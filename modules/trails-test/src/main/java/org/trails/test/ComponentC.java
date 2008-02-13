package org.trails.test;

import java.util.List;


public abstract class ComponentC extends ComponentA
{
	public abstract List getListElements();
	public List getElements() { return getListElements(); }
}