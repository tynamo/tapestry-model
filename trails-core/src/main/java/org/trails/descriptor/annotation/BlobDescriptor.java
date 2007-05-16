package org.trails.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trails.descriptor.BlobDescriptorExtension;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@DescriptorAnnotation(BlobDescriptorAnnotationHandler.class)
public @interface BlobDescriptor
{

	public static final String DEFAULT_fileName = "";
	public static final String DEFAULT_contentType = "";
//    public static final BlobDescriptorExtension.RenderType DEFAULT_renderType = BlobDescriptorExtension.RenderType.LINK;
//    public static final BlobDescriptorExtension.ContentDisposition DEFAULT_contentDisposition = BlobDescriptorExtension.ContentDisposition.INLINE;

	public String fileName() default "";

	public String contentType() default "";

	public BlobDescriptorExtension.RenderType renderType() default BlobDescriptorExtension.RenderType.LINK;

	public BlobDescriptorExtension.ContentDisposition contentDisposition() default BlobDescriptorExtension.ContentDisposition.INLINE;

}
