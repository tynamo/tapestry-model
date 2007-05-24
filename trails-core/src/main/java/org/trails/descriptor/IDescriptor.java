package org.trails.descriptor;

import java.util.Map;

public interface IDescriptor extends Cloneable, IExtensible {
	public boolean isHidden();

	public void setHidden(boolean hidden);

	public String getDisplayName();

	public void setDisplayName(String displayName);

	public void copyFrom(IDescriptor descriptor);

	public void copyExtensionsFrom(IDescriptor descriptor);

	public Object clone();

	public Map<String, IDescriptorExtension> getExtensions();

	public void setExtensions(Map<String, IDescriptorExtension> extensions);
}