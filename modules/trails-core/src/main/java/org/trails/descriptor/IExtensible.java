package org.trails.descriptor;

import java.util.Map;

/**
 * Implementation of the "Extension Object Pattern (Erich Gamma)"
 * Participant: Subject.
 * It declares the interface to query whether an object has a particular extension.
 */
public interface IExtensible
{

	public boolean supportsExtension(String key);

	public void removeExtension(String key);

	public void addExtension(String key, IDescriptorExtension extension);

	public boolean supportsExtension(Class extensionType);

	public void removeExtension(Class extensionType);

	public void addExtension(Class extensionType, IDescriptorExtension extension);

	public IDescriptorExtension getExtension(String key);

	public <E extends IDescriptorExtension> E getExtension(Class<E> extensionType);

    Map<String, IDescriptorExtension> getExtensions();
}
