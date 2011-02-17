package org.tynamo.descriptor.annotation;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.blob.ContentDisposition;
import org.tynamo.blob.RenderType;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@HandledBy("BlobDescriptorAnnotationHandler")
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface BlobDescriptor
{

	public static final String DEFAULT_fileName = "";
	public static final String DEFAULT_contentType = "";

	public String fileName() default "";

	public String contentType() default "";

	public RenderType renderType() default RenderType.LINK;

	public ContentDisposition contentDisposition() default ContentDisposition.INLINE;

}
