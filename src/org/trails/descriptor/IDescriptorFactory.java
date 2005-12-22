package org.trails.descriptor;

public interface IDescriptorFactory
{

    public abstract IClassDescriptor buildClassDescriptor(Class type);

}