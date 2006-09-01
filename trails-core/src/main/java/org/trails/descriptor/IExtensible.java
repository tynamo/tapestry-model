package org.trails.descriptor;

/**
 * Implementation of the "Extension Object Pattern (Erich Gamma)"
 * Participant: Subject.
 * It declares the interface to query whether an object has a particular extension.
 */
public interface IExtensible
{

    public boolean supportsExtension(String extensionType);

    public void removeExtension(String extensionType);

    public void addExtension(String extensionType, IDescriptorExtension extension);

    public IDescriptorExtension getExtension(String extentionType);

    public <E extends IDescriptorExtension> E getExtension(Class<E>  extensionType);
}
