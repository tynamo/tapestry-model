package org.trails.component.blob;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.trails.descriptor.IPropertyDescriptor;

/**
 * @author kenneth.colassi
 */
@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class BlobComponent extends BaseComponent {
    @Parameter(required = true, cache = true)
    public abstract Object getModel();
    public abstract void setModel(Object model);

    @Parameter(required = true, cache = true)
    public abstract IPropertyDescriptor getDescriptor();
    public abstract void setDescriptor(
            IPropertyDescriptor descriptor);

    @Parameter(required = false, cache = true)
    public abstract byte[] getBytes();
    public abstract void setBytes(byte[] bytes);
}