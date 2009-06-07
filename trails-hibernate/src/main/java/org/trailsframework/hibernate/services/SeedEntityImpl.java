package org.trailsframework.hibernate.services;

import java.util.List;

import javax.persistence.Entity;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.descriptor.TrailsClassDescriptor;
import org.trailsframework.descriptor.TrailsPropertyDescriptor;
import org.trailsframework.exception.PersistenceException;
import org.trailsframework.hibernate.services.HibernatePersistenceService;
import org.trailsframework.hibernate.validation.ValidateUniqueness;
import org.trailsframework.services.DescriptorService;

@EagerLoad
public class SeedEntityImpl implements SeedEntity {
	private static final Logger LOGGER = LoggerFactory.getLogger(SeedEntityImpl.class);

	@SuppressWarnings("unchecked")
	public SeedEntityImpl(HibernateSessionManager sessionManager, HibernatePersistenceService persistenceService, DescriptorService descriptorService, List<Object> entities) {
		for (Object entity : entities) {
			if (entity.getClass().getAnnotation(Entity.class) == null) {
				LOGGER.warn("Contributed object '" + entity + "' is not an entity, cannot be used a seed");
				continue;
			}
			TrailsClassDescriptor classDescriptor = null;
			try {
				classDescriptor = descriptorService.getClassDescriptor(entity.getClass());
			} catch (IllegalArgumentException e) {
				LOGGER.error("Seed object " + entity.getClass() + " is not a known entity - cannot be used as a seed ");
				continue;
			}
			if (classDescriptor == null) {
				LOGGER.error("Cannot handle entity of type " + entity.getClass() + " because of non-existent class descriptor");
				continue;
			}
			TrailsPropertyDescriptor identifierDescriptor = classDescriptor.getIdentifierDescriptor();
			Object id = null, savedObject = null;
			String propertyName = identifierDescriptor.getName();
			try {
				id = Ognl.getValue(propertyName, entity);
			} catch (OgnlException e) {
				LOGGER.warn("Couldn't get the id of a seed bean " + entity + " because of: ", e);
			}

			// Try to find if a persistent entity already exists based on unique property or manually set id
			ValidateUniqueness validateUniqueness = entity.getClass().getAnnotation(ValidateUniqueness.class);
			if (validateUniqueness != null || id != null) {
				DetachedCriteria criteria = DetachedCriteria.forClass(entity.getClass());
				if (validateUniqueness != null) {
					propertyName = validateUniqueness.property();

					try {
						Object value = Ognl.getValue(propertyName, entity);
						if (value == null) criteria.add(Restrictions.isNull(propertyName));
						else criteria.add(Restrictions.eq(propertyName, value));
					} catch (OgnlException e) {
						LOGGER.error("Couldn't find if an entity already exists because of: ", e);
					}
				} else criteria.add(Restrictions.eq(propertyName, id));

				savedObject = persistenceService.getInstance(entity.getClass(), criteria);
			}

			if (savedObject != null) {
				try {
					LOGGER.info("Entity of type " + entity.getClass() + " identified by unique property " + propertyName + " " + Ognl.getValue(propertyName, savedObject)
							+ " already exists");
				} catch (OgnlException e) {
					LOGGER.warn("Entity of type " + entity.getClass() + " identified by unique property " + propertyName
							+ " exists, but couldn't display value of identifying property because of: ", e);
				}

				// Need to set the ids to seed beans so a new seed entity with a relationship to existing seed entities can be
				// saved
				try {
					id = Ognl.getValue(identifierDescriptor.getName(), savedObject);
					Ognl.setValue(identifierDescriptor.getName(), entity, id);
				} catch (OgnlException e) {
					LOGGER.warn("Couldn't set the id of an already existing entity because of: ", e);
				}
				continue;
			}
			try {
				persistenceService.saveOrUpdate(entity);
			} catch (InvalidStateException ivex) {
				StringBuilder errorMessageBuilder = new StringBuilder();
				for (InvalidValue invalidValue : ivex.getInvalidValues()) {
					String message = invalidValue.getPropertyName() + ": " + invalidValue.getMessage();
					LOGGER.error(message);
					errorMessageBuilder.append(message).append("\n");
				}
				throw new PersistenceException(errorMessageBuilder.toString(), ivex);
			}
		}
		sessionManager.commit();
	}
}
