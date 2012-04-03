package org.tynamo.model.jpa.internal;

import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.tapestry5.ioc.ObjectCreator;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.PlasticProxyFactory;
import org.apache.tapestry5.jpa.EntityManagerManager;
import org.tynamo.model.jpa.TynamoJpaSymbols;

public class ConfigurableEntityManagerProvider {
	private EntityManagerManager entityManagerManager;
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
		// EntityManager entityManager = null;
		// if (persistenceUnitName.isEmpty()) {
		// if (entityManagerManager.getEntityManagers().size() != 1)
		// throw new IllegalArgumentException(
		// "You have to specify the persistenceunit for JPA model if multiple persistence units are configured in the system. Contribute a value for TynamoJPASymbols.PERSISTENCEUNIT");
		// entityManager = entityManagerManager.getEntityManagers().values().iterator().next();
		// } else {
		// entityManager = entityManagerManager.getEntityManager(persistenceUnitName);
		// if (entityManager == null)
		// throw new IllegalArgumentException(
		// "Persistence unit '"
		// + persistenceUnitName
		// + "' is configured for JPA model, but it was not found. Check that the contributed name matches with persistenceunit configuration");
		// }
		// return entityManager;
	}

	private synchronized EntityManager getOrCreateProxy(final String unitName, final ObjectLocator objectLocator) {
		if (proxy == null) {
			final PlasticProxyFactory proxyFactory = objectLocator.getService("PlasticProxyFactory",
				PlasticProxyFactory.class);

			proxy = proxyFactory.createProxy(EntityManager.class, new ObjectCreator() {
				public Object createObject() {
					final EntityManagerManager entityManagerManager = objectLocator.getService(EntityManagerManager.class);

					if (InternalUtils.isNonBlank(unitName)) return entityManagerManager.getEntityManager(unitName);

					Map<String, EntityManager> entityManagers = entityManagerManager.getEntityManagers();

					if (entityManagers.size() == 1) return entityManagers.values().iterator().next();

					throw new RuntimeException(
						"Unable to locate a single EntityManager. "
							+ "You must provide the persistence unit name as defined in the persistence.xml using the @PersistenceContext annotation.");
				}
			}, "<EntityManagerProxy>");
		}

		return proxy;
	}

}
