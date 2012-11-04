/**
 * Copyright 2011 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Felipe Oliveira (http://mashup.fm)
 *
 */
package org.tynamo.model.elasticsearch.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

/**
 * The Interface ElasticSearchable.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@HandledBy("ElasticSearchAnnotationHandler")
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface ElasticSearchable {
	/** The index name to use */
	String indexName() default "";
}