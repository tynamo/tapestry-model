package org.tynamo.model.jpa.internal;

import javax.persistence.EntityManager;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.jpa.EntityManagerManager;
import org.tynamo.model.jpa.TynamoJPASymbols;

public class ConfigurableEntityManagerProvider {
	private EntityManagerManager entityManagerManager;
	private String persistenceUnitName;

	public ConfigurableEntityManagerProvider(EntityManagerManager entityManagerManager, @Inject @Symbol(TynamoJPASymbols.PERSISTENCEUNIT) String persistenceUnitName) {
		persistenceUnitName = persistenceUnitName;
		entityManagerManager = entityManagerManager;
	}
	
	public EntityManager getEntityManager() {
		EntityManager entityManager = null;
		if (persistenceUnitName.isEmpty()) {
			if (entityManagerManager.getEntityManagers().size() != 1)
				throw new IllegalArgumentException(
					"You have to specify the persistenceunit for JPA model if multiple persistence units are configured in the system. Contribute a value for TynamoJPASymbols.PERSISTENCEUNIT");
			entityManager = entityManagerManager.getEntityManagers().values().iterator().next();
		} else {
			entityManager = entityManagerManager.getEntityManager(persistenceUnitName);
			if (entityManager == null)
				throw new IllegalArgumentException(
					"Persistence unit '"
						+ persistenceUnitName
						+ "' is configured for JPA model, but it was not found. Check that the contributed name matches with persistenceunit configuration");
		}
		return entityManager;
	}

}
