package org.trails.descriptor;

import org.trails.component.blob.ITrailsBlob;
import org.trails.TrailsRuntimeException;

public class BlobDescriptorExtension implements IDescriptorExtension {

    public enum ContentDisposition {

        INLINE("inline"), ATTACHMENT("attachment");

        private String value = "";

        ContentDisposition(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum RenderType {
        IMAGE, LINK, IFRAME;

        public boolean isImage() {
            return this == IMAGE;
        }

        public boolean isLink() {
            return this == LINK;
        }

        public boolean isIFrame() {
            return this == IFRAME;
        }
    }

    private enum BlobType {
        BYTES, ITRAILSBLOB
    }

    private BlobType blobType = BlobType.BYTES;
    private String fileName = "";
    private String contentType = "";
    private ContentDisposition contentDisposition = ContentDisposition.INLINE;
    private RenderType renderType = RenderType.LINK;


    public BlobDescriptorExtension(Class type) {
        if (ITrailsBlob.class.isAssignableFrom(type)) {
            blobType = BlobType.ITRAILSBLOB;
        } else if (type.isArray()) {
            blobType = BlobType.BYTES;
        } else {
            throw new TrailsRuntimeException("type: " + type + " - Not supported");
        }
    }

    public boolean isBytes() {
        return blobType == BlobType.BYTES;
    }

    public boolean isITrailsBlob() {
        return blobType == BlobType.ITRAILSBLOB;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
    }
}
