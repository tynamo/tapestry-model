package org.tynamo.model.jpa.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.model.elasticsearch.annotations.handlers.ElasticSearchAnnotationHandler;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.model.elasticsearch.mapping.impl.DefaultMapperFactory;
import org.tynamo.model.jpa.TynamoJpaSymbols;
import org.tynamo.model.jpa.internal.ElasticSearchIndexMaintainer;

public class ElasticSearchModule {

	public static void bind(ServiceBinder binder) {

		binder.bind(MapperFactory.class, DefaultMapperFactory.class);
		binder.bind(DescriptorAnnotationHandler.class, ElasticSearchAnnotationHandler.class)
				.withId("ElasticSearchAnnotationHandler");
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(TynamoJpaSymbols.ELASTICSEARCH_HOME, "");
		configuration.add(TynamoJpaSymbols.ELASTICSEARCH_HTTP_ENABLED, false);
	}

	public Node buildNode(@Symbol(TynamoJpaSymbols.ELASTICSEARCH_HOME) String pathHome,
	                      @Symbol(TynamoJpaSymbols.ELASTICSEARCH_HTTP_ENABLED) boolean httpEnabled,
		RegistryShutdownHub registryShutdownHub) throws UnknownHostException {
		Settings.Builder settings = Settings.settingsBuilder();
		if (!pathHome.isEmpty()) settings.put("path.home", pathHome);
		settings.put("http.enabled", httpEnabled);
		settings.put("number_of_shards", 1);
		settings.put("number_of_replicas", 0);
		settings.put("cluster.name", "tynamo-model-search-" + InetAddress.getLocalHost().getHostName()).build();
		final Node node = NodeBuilder.nodeBuilder().local(true).data(true).settings(settings).build();
		node.start();

		registryShutdownHub.addRegistryShutdownListener(new Runnable() {
			@Override
			public void run() {
				node.close(); // TYNAMO-223
			}
		});

		return node;
	}

	@Startup
	public static void addJpaEventListener(@Autobuild ElasticSearchIndexMaintainer indexMaintainer) {
		indexMaintainer.start();
	}
}
