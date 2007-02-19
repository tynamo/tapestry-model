package org.trails.component.blob;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.BlobDescriptorExtension;
import org.trails.descriptor.DescriptorService;
import org.trails.persistence.PersistenceService;

public class BlobDownloadService implements IEngineService {

    private static final Log LOG = LogFactory.getLog(BlobDownloadService.class);

    public static final String SERVICE_NAME = "BlobService";

    private RequestExceptionReporter _exceptionReporter;

    private LinkFactory _linkFactory;

    private WebResponse _response;

    private PersistenceService persistenceService;

    private DescriptorService descriptorService;

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public DescriptorService getDescriptorService() {
        return descriptorService;
    }

    public void setDescriptorService(DescriptorService descriptorService) {
        this.descriptorService = descriptorService;
    }

    private static final String BLOBID = "id";

    private static final String ENTITY_NAME = "class";

    private static final String BYTES_PROPERTY = "property";

    private static final String CONTENT_TYPE = "contentType";

    private static final String FILE_NAME = "fileName";

    public ILink getLink(boolean post, Object parameter) {

        Defense.isAssignable(((Object[]) parameter)[0], TrailsBlobAsset.class,
                "parameter");

        TrailsBlobAsset asset = (TrailsBlobAsset) ((Object[]) parameter)[0];

        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put(BLOBID, String.valueOf(asset.getIdProperty()));
        parameters.put(ENTITY_NAME, asset.getEntityName());
        parameters.put(BYTES_PROPERTY, asset.getBytesProperty());

        if (asset.getFileName() != null) {
            parameters.put(FILE_NAME, asset.getFileName());
        }

        if (asset.getContentType() != null) {
            parameters.put(CONTENT_TYPE, asset.getContentType());
        }

        return _linkFactory.constructLink(this, false, parameters, true);

    }

    public void service(IRequestCycle cycle) throws IOException {

        String blobID = cycle.getParameter(BLOBID);
        String entityName = cycle.getParameter(ENTITY_NAME);
        String bytesProp = cycle.getParameter(BYTES_PROPERTY);
        String fileName = cycle.getParameter(FILE_NAME);
        String contentType = cycle.getParameter(CONTENT_TYPE);

        try {
            BlobDescriptorExtension blobDescriptor = getDescriptorService()
                    .getClassDescriptor(Class.forName(entityName))
                    .getPropertyDescriptor(bytesProp).getExtension(
                            BlobDescriptorExtension.class);

            if (blobDescriptor != null && blobID != null && !"".equals(blobID)) {
                Object model = getPersistenceService().getInstance(
                        Class.forName(entityName), Integer.valueOf(blobID));
                if (model != null) {
                    byte[] bytes = new byte[0];

                    if (blobDescriptor.isBytes()) {
                        if (fileName == null) {
                            if (!"".equals(blobDescriptor.getFileName()))
                                fileName = blobDescriptor.getFileName();
                            else
                                fileName = ((ITrailsBlob) model).getFileName();
                        }
                        if (contentType == null) {
                            if (!"".equals(blobDescriptor.getContentType()))
                                contentType = blobDescriptor.getContentType();
                            else
                                contentType = ((ITrailsBlob) model)
                                        .getContentType();
                        }

                        bytes = (byte[]) Ognl.getValue(bytesProp, model);
                    } else if (blobDescriptor.isITrailsBlob()) {
                        ITrailsBlob trailsBlob = (ITrailsBlob) Ognl.getValue(
                                bytesProp, model);
                        if (trailsBlob != null) {
                            bytes = trailsBlob.getBytes();
                            contentType = !"".equals(blobDescriptor
                                    .getContentType()) ? blobDescriptor
                                    .getContentType() : trailsBlob
                                    .getContentType();
                            fileName = !"".equals(blobDescriptor.getFileName()) ? blobDescriptor
                                    .getFileName()
                                    : trailsBlob.getFileName();
                        }
                    }

                    if (bytes.length > 0) {
                        _response.setHeader("Expires", "0");
                        _response.setHeader("Cache-Control",
                                "must-revalidate, post-check=0,pre-check=0");
                        _response.setHeader("Pragma", "public");
                        _response.setHeader("Content-Disposition",
                                blobDescriptor.getContentDisposition()
                                        .getValue()
                                        + "; filename=" + fileName);
                        _response.setContentLength(bytes.length);

                        OutputStream output = _response
                                .getOutputStream(new ContentType(contentType));
                        output.write(bytes);
                    } else {
                        String errorText = "Entity has no ID or does not exist yet";
                        LOG.info(errorText);
                        throw new TrailsRuntimeException(errorText);
                    }
                }

            } else {
                String errorText = "Entity has no ID or does not exist yet";
                LOG.info(errorText);
                throw new TrailsRuntimeException(errorText);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OgnlException e) {
            e.printStackTrace();
        }

        return;
    }

    public String getName() {
        return SERVICE_NAME;
    }

    public void setExceptionReporter(RequestExceptionReporter exceptionReporter) {
        _exceptionReporter = exceptionReporter;
    }

    public void setLinkFactory(LinkFactory linkFactory) {
        _linkFactory = linkFactory;
    }

    public void setResponse(WebResponse response) {
        _response = response;
    }
}