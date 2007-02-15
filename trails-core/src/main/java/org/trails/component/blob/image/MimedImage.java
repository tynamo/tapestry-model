package org.trails.component.blob.image;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.html.Image;
import org.trails.component.blob.ITrailsBlob;
import org.trails.descriptor.BlobDescriptorExtension;
import org.trails.descriptor.IPropertyDescriptor;

/**
 * This widget overloads the src attribute of @Image
 *
 * It substitutes icons when tapestry fails to negotiate a
 * proper src for uploadable media.
 *
 * This is exclusively intended for org.trails.component.blob.ITrailsBlob
 *
 * @author kenneth.colassi
 *
 */

@ComponentClass(allowBody = false, allowInformalParameters = true)
public abstract class MimedImage extends Image {
    private Map<String,String> map = new HashMap<String,String>();

    @InjectObject("service:tapestry.asset.ClasspathAssetFactory")
    public abstract AssetFactory getClasspathAssetFactory();

    @Parameter(required = true)
    public abstract IPropertyDescriptor getDescriptor();
    public abstract void setDescriptor(
            IPropertyDescriptor descriptor);

    @Parameter(required = true)
    public abstract Object getBytes();
    public abstract void setBytes(Object bytes);

    @Parameter(required = true)
    public abstract Object getModel();
    public abstract void setModel(Object bytes);

    @Parameter(required = true)
    public abstract IAsset getImage();
    public abstract void setImage(IAsset image);

    public BlobDescriptorExtension getBlobDescriptorExtension() {
        return getDescriptor().getExtension(
                BlobDescriptorExtension.class);
    }

    public MimedImage() {
        /**
         * Map keyes MUST adhere to standard mime
         */
        map.put("application/x-zip-compressed",
                "/org/trails/component/blob/image/asset/winzip.gif");
        map.put("application/pdf",
                "/org/trails/component/blob/image/asset/icadobe.gif");
        map.put("application/msword",
                "/org/trails/component/blob/image/asset/icdoc.gif");
        map.put("application/vnd.visio",
                "/org/trails/component/blob/image/asset/icdoc.gif");
        map.put("application/vnd.ms-powerpoint",
                "/org/trails/component/blob/image/asset/icppt.gif");
        map.put("application/vnd.ms-excel",
                "/org/trails/component/blob/image/asset/icxls.gif");
        map.put("application/octet-stream",
                "/org/trails/component/blob/image/asset/icgen.gif");

        map.put("text/html", "/org/trails/component/blob/image/asset/ichtm.gif");
        map.put("text/plain", "/org/trails/component/blob/image/asset/ictxt.gif");
        map.put("text/css", "/org/trails/component/blob/image/asset/ictxt.gif");
        map.put("text/xml", "/org/trails/component/blob/image/asset/icxml.gif");

        map.put("image/tiff", "/org/trails/component/blob/image/asset/icgen.gif");

        map.put("video/avi", "/org/trails/component/blob/image/asset/icwmp.gif");
        map.put("video/mpeg", "/org/trails/component/blob/image/asset/icwmp.gif");
        map.put("video/mp4", "/org/trails/component/blob/image/asset/icwmp.gif");
        map.put("video/quicktime",
                "/org/trails/component/blob/image/asset/icwmp.gif");
        map
                .put("video/x-ms-wmv",
                        "/org/trails/component/blob/image/asset/icwmp.gif");

    }

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        if (cycle.isRewinding())
            return;

        IAsset imageAsset = getImage();

        if (imageAsset == null)
            throw Tapestry.createRequiredParameterException(this, "image");

        writer.beginEmpty("img");

        ITrailsBlob trailsBlob = null;
        if (getBlobDescriptorExtension().isBytes()) {
            trailsBlob = (ITrailsBlob) getModel();
        } else if (getBlobDescriptorExtension().isITrailsBlob()) {
            trailsBlob = (ITrailsBlob) getBytes();
        }
        String contentType = trailsBlob.getContentType();

        if (contentType == null) {
            writer.attribute("src", imageAsset.buildURL());
        } else if (contentType.equalsIgnoreCase("image/pjpeg")
                || contentType.equalsIgnoreCase("image/gif")) {
            writer.attribute("src", imageAsset.buildURL());
        } else {
            AssetFactory fact = getClasspathAssetFactory();

            IAsset asset;

            if (map.containsKey(contentType)) {
                asset = fact.createAbsoluteAsset(map.get(contentType)
                        .toString(), null, null);
                writer.attribute("src", asset.buildURL());
            } else {
                asset = fact.createAbsoluteAsset(map.get(
                        "application/octet-stream").toString(), null, null);
                writer.attribute("src", asset.buildURL());
            }
        }

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }
}
