package org.tynamo.descriptor.annotation;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.BlobDescriptorAnnotationHandler;
import org.tynamo.descriptor.annotation.handlers.HandledBy;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@HandledBy(BlobDescriptorAnnotationHandler.class)
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface BlobDescriptor
{

	public static final String DEFAULT_fileName = "";
	public static final String DEFAULT_contentType = "";

	public String fileName() default "";

	public String contentType() default "";

	public BlobDescriptorExtension.RenderType renderType() default BlobDescriptorExtension.RenderType.LINK;

	public BlobDescriptorExtension.ContentDisposition contentDisposition() default BlobDescriptorExtension.ContentDisposition.INLINE;

}
