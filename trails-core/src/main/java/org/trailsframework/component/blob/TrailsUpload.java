package org.trails.component.blob;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.request.IUploadFile;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class TrailsUpload extends BaseComponent
{
	@InjectObject("service:trails.core.FilePersister")
	public abstract IFilePersister getFilePersister();

	@Parameter(required = true)
	public abstract Object getModel();

	@Parameter(required = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public IUploadFile getFile()
	{
		return null;
	}

	public void setFile(IUploadFile file)
	{
		if (file != null)
		{
			getFilePersister().store(getPropertyDescriptor(), getModel(), file);
		}
	}
}
