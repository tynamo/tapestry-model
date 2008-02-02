package org.trails.component.blob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.extension.BlobDescriptorExtension;
import org.trails.persistence.PersistenceService;
import org.trails.util.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class BlobDownloadService implements IEngineService
{

	private static final Log LOG = LogFactory.getLog(BlobDownloadService.class);

	public static final String SERVICE_NAME = "BlobService";

	private LinkFactory _linkFactory;

	private WebResponse _response;

	private PersistenceService persistenceService;

	private DescriptorService descriptorService;

	private IFilePersister filePersister;

	private static final String BLOBID = "id";

	private static final String ENTITY_NAME = "class";

	private static final String BYTES_PROPERTY = "property";

	public ILink getLink(boolean post, Object parameter)
	{

		Defense.isAssignable(((Object[]) parameter)[0], TrailsBlobAsset.class, "parameter");

		TrailsBlobAsset asset = (TrailsBlobAsset) ((Object[]) parameter)[0];

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(BLOBID, String.valueOf(asset.getIdProperty()));
		parameters.put(ENTITY_NAME, asset.getEntityName());
		parameters.put(BYTES_PROPERTY, asset.getBytesProperty());

		return _linkFactory.constructLink(this, false, parameters, true);

	}

	public void service(IRequestCycle cycle) throws IOException
	{
		String blobID = cycle.getParameter(BLOBID);
		String entityName = cycle.getParameter(ENTITY_NAME);
		String bytesProp = cycle.getParameter(BYTES_PROPERTY);

		IClassDescriptor classDescriptor = descriptorService.getClassDescriptor(Utils.classForName(entityName));
		IPropertyDescriptor propertyDescriptor = classDescriptor.getPropertyDescriptor(bytesProp);
		BlobDescriptorExtension blobDescriptor = propertyDescriptor.getExtension(BlobDescriptorExtension.class);

		if (blobDescriptor != null && blobID != null && !"".equals(blobID))
		{
			Object model = persistenceService.getInstance(classDescriptor.getType(), Integer.valueOf(blobID));

			if (model != null)
			{
				String fileName = filePersister.getFileName(classDescriptor, propertyDescriptor, model);
				String contentType = filePersister.getContentType(classDescriptor, propertyDescriptor, model);
				byte[] bytes = filePersister.getData(classDescriptor, propertyDescriptor, model);

				if (bytes.length > 0)
				{
					_response.setHeader("Expires", "0");
					_response.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
					_response.setHeader("Pragma", "public");
					_response.setHeader("Content-Disposition",
							blobDescriptor.getContentDisposition().getValue() + (fileName != null ? "; filename=" + fileName : ""));
					_response.setContentLength(bytes.length);

					OutputStream output = _response.getOutputStream(contentType != null ? new ContentType(contentType) : new ContentType());
					output.write(bytes);
				} else
				{
					String errorText = "BlobDownloadServcie: entityName->" + entityName + ", blobID ->" +
							blobID + " : has not been ingested yet";
					LOG.info(errorText);
//							 muted kwc - throw new TrailsRuntimeException(errorText);
				}
			}

		} else
		{
			String errorText = "BlobDownloadServcie: entityName->" + entityName + ", blobID ->" + blobID +
					" : has not been ingested yet";
			LOG.info(errorText);
			// muted kwc - throw new TrailsRuntimeException(errorText);
		}
	}

	public String getName()
	{
		return SERVICE_NAME;
	}

	public void setLinkFactory(LinkFactory linkFactory)
	{
		_linkFactory = linkFactory;
	}

	public void setResponse(WebResponse response)
	{
		_response = response;
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public void setFilePersister(IFilePersister filePersister)
	{
		this.filePersister = filePersister;
	}
}