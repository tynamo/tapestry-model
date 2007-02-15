package org.trails.component.blob;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.trails.descriptor.BlobDescriptorExtension;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class TrailsDownload extends BaseComponent
{
    @InjectObject("service:trails.BlobService")
    public abstract BlobDownloadService getBlobService();

    @Parameter(required = true, cache = true)
    public abstract Object getBytes();
    public abstract void setBytes(Object bytes);

    @Parameter(required = true, cache = true)
    public abstract Object getModel();
    public abstract void setModel(Object bytes);

    @Parameter(required = false, cache = true)
    public abstract String getFileName();
    public abstract void setFileName(String fileName);

    @Parameter(required = false, cache = true)
    public abstract String getContentType();
    public abstract void setContentType(String contentType);

    @Parameter(required = false, defaultValue = "page.classDescriptor", cache = true)
    public abstract IClassDescriptor getClassDescriptor();
    public abstract void setClassDescriptor(IClassDescriptor ClassDescriptor);

    @Parameter(required = true, cache = true)
    public abstract IPropertyDescriptor getDescriptor();
    public abstract void setDescriptor(IPropertyDescriptor descriptor);

    public IPropertyDescriptor getIdentifierDescriptor() {
        return getClassDescriptor().getIdentifierDescriptor();
    }

    public BlobDescriptorExtension getBlobDescriptorExtension() {
        return getDescriptor().getExtension(BlobDescriptorExtension.class);
    }

    public IAsset getByteArrayAsset() {
        String id = "";
        try
        {
            id = Ognl.getValue(getIdentifierDescriptor().getName(), getModel()).toString();
        } catch (OgnlException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NullPointerException npe)
        {
            id = "";
        }

        ITrailsBlob trailsBlob = null;
        if (getBlobDescriptorExtension().isBytes()) {
            trailsBlob = (ITrailsBlob) getModel();
        } else if (getBlobDescriptorExtension().isITrailsBlob()) {
            trailsBlob = (ITrailsBlob) getBytes();
        }

        return new TrailsBlobAsset(getBlobService(), getClassDescriptor().getType().getName(), id, getDescriptor().getName(), trailsBlob.getContentType(), trailsBlob.getFileName());
    }
}
