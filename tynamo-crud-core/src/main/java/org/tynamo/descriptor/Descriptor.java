package org.tynamo.descriptor;

public interface Descriptor extends Cloneable, Extensible
{
	/**
	 * @return
	 */
	public boolean isHidden();

	public void setHidden(boolean hidden);

	public Object clone();

}