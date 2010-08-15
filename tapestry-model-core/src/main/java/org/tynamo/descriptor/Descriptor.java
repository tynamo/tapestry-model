package org.tynamo.descriptor;

import org.tynamo.descriptor.extension.Extensible;

public interface Descriptor extends Cloneable, Extensible
{
	/**
	 * @return
	 */
	public boolean isHidden();

	public void setHidden(boolean hidden);

	public Object clone();

}