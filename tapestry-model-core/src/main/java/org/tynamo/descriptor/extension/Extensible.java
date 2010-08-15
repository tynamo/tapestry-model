package org.tynamo.descriptor.extension;

import org.tynamo.descriptor.extension.DescriptorExtension;

import java.util.Map;

/**
 * Implementation of the "Extension Object Pattern (Erich Gamma)"
 * Participant: Subject.
 * It declares the interface to query whether an object has a particular extension.
 */
public interface Extensible
{

	public boolean supportsExtension(String key);

	public void removeExtension(String key);

	public void addExtension(String key, DescriptorExtension extension);

	public boolean supportsExtension(Class extensionType);

	public void removeExtension(Class extensionType);

	public void addExtension(Class extensionType, DescriptorExtension extension);

	public DescriptorExtension getExtension(String key);

	public <E extends DescriptorExtension> E getExtension(Class<E> extensionType);

    Map<String, DescriptorExtension> getExtensions();
}
