package org.tynamo.model.jpa.internal;

import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.tapestry5.commons.ObjectCreator;
import org.apache.tapestry5.commons.ObjectLocator;
import org.apache.tapestry5.commons.services.PlasticProxyFactory;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.jpa.EntityManagerManager;
import org.tynamo.model.jpa.TynamoJpaSymbols;

public class ConfigurableEntityManagerProvider {

	private String persistenceUnitName;
	private ObjectLocator locator;
	private EntityManager proxy;

	public ConfigurableEntityManagerProvider(ObjectLocator locator,
		@Inject @Symbol(TynamoJpaSymbols.PERSISTENCEUNIT) String persistenceUnitName) {
		this.locator = locator;
		this.persistenceUnitName = persistenceUnitName;
	}

	public EntityManager getEntityManager() {
		if (proxy != null) return proxy;
		return getOrCreateProxy(persistenceUnitName, locator);
	}

	private synchronized EntityManager getOrCreateProxy(final String unitName, final ObjectLocator objectLocator) {
		if (proxy == null) {
			final PlasticProxyFactory proxyFactory = objectLocator.getService("PlasticProxyFactory", PlasticProxyFactory.class);

			proxy = proxyFactory.createProxy(EntityManager.class, new ObjectCreator<EntityManager>() {
				public EntityManager createObject() {
					final EntityManagerManager entityManagerManager = objectLocator.getService(EntityManagerManager.class);

					if (InternalUtils.isNonBlank(unitName)) return entityManagerManager.getEntityManager(unitName);

					Map<String, EntityManager> entityManagers = entityManagerManager.getEntityManagers();

					if (entityManagers.size() == 1) return entityManagers.values().iterator().next();

					throw new RuntimeException(
							"Unable to locate a single EntityManager. "
									+ "You must provide the persistence unit name using the TynamoJpaSymbols.PERSISTENCEUNIT symbol");
				}
			}, "<EntityManagerProxy>");
		}

		return proxy;
	}

}
