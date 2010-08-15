package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.annotation.handlers.BlobDescriptorAnnotationHandler;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@DescriptorAnnotation(BlobDescriptorAnnotationHandler.class)
public @interface BlobDescriptor
{

	public static final String DEFAULT_fileName = "";
	public static final String DEFAULT_contentType = "";

	public String fileName() default "";

	public String contentType() default "";

	public BlobDescriptorExtension.RenderType renderType() default BlobDescriptorExtension.RenderType.LINK;

	public BlobDescriptorExtension.ContentDisposition contentDisposition() default BlobDescriptorExtension.ContentDisposition.INLINE;

}
