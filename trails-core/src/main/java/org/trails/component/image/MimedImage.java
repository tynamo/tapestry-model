package org.trails.component.image;

import java.util.HashMap;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.html.Image;

/**
 * This widget overloads the src attribute of @Image .
 *
 * It serves as a surrogate intermediary when tapestry fails to
 * negotiate a proper src for uploadable media.
 *
 * This widget can be used anywhere a developer needs but was exclusively
 * developed to supplement org.trails.component.blob.ITrailsBlob usage
 *
 * @author kenneth.colassi
 *
 */

@ComponentClass(allowBody = false, allowInformalParameters = false)
public abstract class MimedImage extends Image {
    @InjectObject("service:tapestry.asset.ClasspathAssetFactory")
    public abstract AssetFactory getClasspathAssetFactory();

    private HashMap map = new HashMap();

    @Parameter(required = true)
    public abstract IAsset getImage();
    public abstract void setImage(IAsset image);

    @Parameter(required = true)
    public abstract String getContentType();
    public abstract void setContentType(String contentType);

    public MimedImage() {
        /**
         * Map keyes MUST adhere to standard mime
         */
        map.put("application/x-zip-compressed", "/org/trails/component/image/asset/winzip.gif");
        map.put("application/pdf", "/org/trails/component/image/asset/icadobe.gif");
        map.put("application/msword", "/org/trails/component/image/asset/icdoc.gif");
        map.put("application/vnd.visio", "/org/trails/component/image/asset/icdoc.gif");
        map.put("application/vnd.ms-powerpoint", "/org/trails/component/image/asset/icppt.gif");
        map.put("application/vnd.ms-excel", "/org/trails/component/image/asset/icxls.gif");
        map.put("application/octet-stream", "/org/trails/component/image/asset/icgen.gif");

        map.put("text/html", "/org/trails/component/image/asset/ichtm.gif");
        map.put("text/plain", "/org/trails/component/image/asset/ictxt.gif");
        map.put("text/css", "/org/trails/component/image/asset/ictxt.gif");
        map.put("text/xml", "/org/trails/component/image/asset/icxml.gif");

        map.put("image/tiff", "/org/trails/component/image/asset/icgen.gif");

        map.put("video/avi", "/org/trails/component/image/asset/icwmp.gif");
        map.put("video/mpeg", "/org/trails/component/image/asset/icwmp.gif");
        map.put("video/mp4", "/org/trails/component/image/asset/icwmp.gif");
        map.put("video/quicktime", "/org/trails/component/image/asset/icwmp.gif");
        map.put("video/x-ms-wmv", "/org/trails/component/image/asset/icwmp.gif");

    }

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        if (cycle.isRewinding())
            return;

        IAsset imageAsset = getImage();
        String contentType = getContentType();

        if (imageAsset == null)
            throw Tapestry.createRequiredParameterException(this, "image");

        writer.beginEmpty("img");

        if (contentType.equalsIgnoreCase("image/pjpeg")
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
