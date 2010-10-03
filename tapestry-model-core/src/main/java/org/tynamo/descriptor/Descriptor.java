package org.tynamo.descriptor;

import org.tynamo.descriptor.extension.Extensible;

public interface Descriptor extends Cloneable, Extensible
{
	public boolean isNonVisual();

	public void setNonVisual(boolean nonVisual);

	public Object clone();

}