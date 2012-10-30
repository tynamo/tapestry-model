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
package org.tynamo.model.elasticsearch.adapter;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.model.elasticsearch.mapping.MappingUtil;
import org.tynamo.model.elasticsearch.mapping.ModelMapper;
import org.tynamo.model.elasticsearch.util.ReflectionUtil;

/**
 * The Class ElasticSearchAdapter.
 */
@Deprecated
// delete this class when you've migrated the operations elsewhere
public abstract class ElasticSearchAdapter {
	public static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

	/**
	 * Start index.
	 * 
	 * @param client
	 *          the client
	 * @param mapper
	 *          the model mapper
	 */
	public static void startIndex(Client client, ModelMapper mapper) {
		createIndex(client, mapper);
		createType(client, mapper);
	}

	/**
	 * Creates the index.
	 * 
	 * @param client
	 *          the client
	 * @param mapper
	 *          the model mapper
	 */
	private static void createIndex(Client client, ModelMapper mapper) {
		String indexName = mapper.getIndexName();

		try {
			logger.debug("Starting Elastic Search Index %s", indexName);
			CreateIndexResponse response = client.admin().indices()
					.create(new CreateIndexRequest(indexName)).actionGet();
			logger.debug("Response: %s", response);

		} catch (IndexAlreadyExistsException iaee) {
			logger.debug("Index already exists: %s", indexName);

		} catch (Throwable t) {
			logger.warn(ExceptionUtils.getStackTrace(t));
		}
	}

	/**
	 * Creates the type.
	 * 
	 * @param client
	 *          the client
	 * @param mapper
	 *          the model mapper
	 */
	private static void createType(Client client, ModelMapper mapper) {
		String indexName = mapper.getIndexName();
		String typeName = mapper.getTypeName();

		try {
			logger.debug("Create Elastic Search Type %s/%s", indexName, typeName);
			PutMappingRequest request = Requests.putMappingRequest(indexName).type(typeName);
			XContentBuilder mapping = MappingUtil.getMapping(mapper);
			logger.debug("Type mapping: \n %s", mapping.string());
			request.source(mapping);
			PutMappingResponse response = client.admin().indices().putMapping(request).actionGet();
			logger.debug("Response: %s", response);

		} catch (IndexAlreadyExistsException iaee) {
			logger.debug("Index already exists: %s", indexName);

		} catch (Throwable t) {
			logger.warn(ExceptionUtils.getStackTrace(t));
		}
	}

	/**
	 * Index model.
	 * 
	 * @param <T>
	 *          the generic type
	 * @param client
	 *          the client
	 * @param mapper
	 *          the model mapper
	 * @param model
	 *          the model
	 * @throws Exception
	 *           the exception
	 */
	public static void indexModel(Client client, ModelMapper mapper, Object model)
			throws Exception {
		logger.debug("Index Model: %s", model);

		// Check Client
		if (client == null) {
			logger.error("Elastic Search Client is null, aborting");
			return;
		}

		// Define Content Builder
		XContentBuilder contentBuilder = null;

		// Index Model
		try {
			// Define Index Name
			String indexName = mapper.getIndexName();
			String typeName = mapper.getTypeName();
			String documentId = mapper.getDocumentId(model);
			logger.debug("Index Name: %s", indexName);

			contentBuilder = XContentFactory.jsonBuilder().prettyPrint();
			mapper.addModel(model, contentBuilder);
			logger.debug("Index json: %s", contentBuilder.string());
			IndexResponse response = client.prepareIndex(indexName, typeName, documentId)
					.setSource(contentBuilder).execute().actionGet();

			// Log Debug
			logger.info("Index Response: %s", response);

		} finally {
			if (contentBuilder != null) {
				contentBuilder.close();
			}
		}
	}

	/**
	 * Delete model.
	 * 
	 * @param <T>
	 *          the generic type
	 * @param client
	 *          the client
	 * @param mapper
	 *          the model mapper
	 * @param model
	 *          the model
	 * @throws Exception
	 *           the exception
	 */
	public static void deleteModel(Client client, ModelMapper mapper, Object model)
			throws Exception {
		logger.debug("Delete Model: %s", model);
		String indexName = mapper.getIndexName();
		String typeName = mapper.getTypeName();
		String documentId = mapper.getDocumentId(model);
		DeleteResponse response = client.prepareDelete(indexName, typeName, documentId)
				.setOperationThreaded(false).execute().actionGet();
		logger.debug("Delete Response: %s", response);

	}

}
