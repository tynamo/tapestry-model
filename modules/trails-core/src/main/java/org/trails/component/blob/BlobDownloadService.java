package org.trails.component.blob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.extension.BlobDescriptorExtension;
import org.trails.persistence.PersistenceService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BlobDownloadService implements IEngineService
{

	private static final Log LOG = LogFactory.getLog(BlobDownloadService.class);

	public static final String SERVICE_NAME = "download";

	private LinkFactory _linkFactory;

	private WebResponse _response;

	private PersistenceService persistenceService;

	private IFilePersister filePersister;

	DataSqueezer dataSqueezer;

	private static final String BLOBID_PARAMETER_NAME = "id";

	private static final String PROP_DESC_PARAMETER_NAME = "propDesc";

	public ILink getLink(boolean post, Object parameter)
	{

		Defense.isAssignable(((Object[]) parameter)[0], TrailsBlobAsset.class, "parameter");

		TrailsBlobAsset asset = (TrailsBlobAsset) ((Object[]) parameter)[0];

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PROP_DESC_PARAMETER_NAME, dataSqueezer.squeeze(asset.getPropertyDescriptor()));
		parameters.put(BLOBID_PARAMETER_NAME, dataSqueezer.squeeze(asset.getId()));

		return _linkFactory.constructLink(this, false, parameters, true);

	}

	public void service(IRequestCycle cycle) throws IOException
	{
		String blobID = cycle.getParameter(BLOBID_PARAMETER_NAME);
		String bytesProp = cycle.getParameter(PROP_DESC_PARAMETER_NAME);

		IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) dataSqueezer.unsqueeze(bytesProp);
		BlobDescriptorExtension blobDescriptor = propertyDescriptor.getExtension(BlobDescriptorExtension.class);

		if (blobDescriptor != null && blobID != null && !"".equals(blobID))
		{
			Object model = persistenceService
					.getInstance(propertyDescriptor.getBeanType(), (Serializable) dataSqueezer.unsqueeze(blobID));

			if (model != null)
			{
				String fileName = filePersister.getFileName(propertyDescriptor, model);
				String contentType = filePersister.getContentType(propertyDescriptor, model);
				byte[] bytes = filePersister.getData(propertyDescriptor, model);

				if (bytes.length > 0)
				{
					_response.setHeader("Expires", "0");
					_response.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
					_response.setHeader("Pragma", "public");
					_response.setHeader("Content-Disposition",
							blobDescriptor.getContentDisposition().getValue() +
									(fileName != null ? "; filename=" + fileName : ""));
					_response.setContentLength(bytes.length);

					OutputStream output = null;
					try
					{
						output = _response.getOutputStream(
								contentType != null ? new ContentType(contentType) : new ContentType());
						output.write(bytes);
					} finally
					{
						try
						{
							if (output != null)
							{
								output.flush();
								output.close();
							}
						} catch (Throwable t)
						{
							// do nothing;
						}
					}
				} else
				{
					String errorText = "BlobDownloadServcie: entityName->" +
							propertyDescriptor.getBeanType().getName() + ", blobID ->" + blobID +
							" : has not been ingested yet";
					LOG.info(errorText);
//							 muted kwc - throw new TrailsRuntimeException(errorText);
				}
			}

		} else
		{
			String errorText = "BlobDownloadServcie: entityName->" + propertyDescriptor.getBeanType().getName() +
					", blobID ->" + blobID +
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

	public void setFilePersister(IFilePersister filePersister)
	{
		this.filePersister = filePersister;
	}

	public void setDataSqueezer(DataSqueezer dataSqueezer)
	{
		this.dataSqueezer = dataSqueezer;
	}
}