package org.trailsframework.descriptor;

public interface IDescriptor extends Cloneable, IExtensible
{
	/**
	 * @return
	 */
	public boolean isHidden();

	public void setHidden(boolean hidden);

	public Object clone();

}