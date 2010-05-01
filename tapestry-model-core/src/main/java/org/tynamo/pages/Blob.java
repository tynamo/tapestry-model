package org.tynamo.pages;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.Response;
import org.tynamo.blob.BlobManager;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;
import org.tynamo.services.DescriptorService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Blob
{
	@Inject
	private BlobManager filePersister;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private ContextValueEncoder contextValueEncoder;

	protected StreamResponse onActivate(Class clazz, String id, String property) throws Exception
	{
		final Object bean = contextValueEncoder.toValue(clazz, id);
		final TynamoPropertyDescriptor propertyDescriptor =
				descriptorService.getClassDescriptor(clazz).getPropertyDescriptor(property);

		return new StreamResponse()
		{
			public String getContentType()
			{
				return filePersister.getContentType(propertyDescriptor, bean);
			}

			public InputStream getStream() throws IOException
			{
				return new ByteArrayInputStream(filePersister.getData(propertyDescriptor, bean));
			}

			public void prepareResponse(Response response)
			{
				String contentDisposition =
						propertyDescriptor.getExtension(BlobDescriptorExtension.class).getContentDisposition().getValue();
				String fileName = filePersister.getFileName(propertyDescriptor, bean);
				response.setHeader("Content-Disposition",
						contentDisposition + (fileName != null ? "; filename=" + fileName : ""));
			}
		};
	}
}
