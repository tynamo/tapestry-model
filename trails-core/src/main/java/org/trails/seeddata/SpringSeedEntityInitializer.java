package org.trails.seeddata;


import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceService;
import org.trails.validation.ValidateUniqueness;

import javax.persistence.Entity;

public class SpringSeedEntityInitializer implements ApplicationContextAware, SeedDataInitializer {
	private static final Log log = LogFactory.getLog(SpringSeedEntityInitializer.class);

	private PersistenceService persistenceService;

	private ApplicationContext applicationContext;

	private DescriptorService descriptorService;

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setDescriptorService(DescriptorService descriptorService) {
		this.descriptorService = descriptorService;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/* (non-Javadoc)
	 * @see org.trails.seeddata.SeedDataInitializer#init()
	 */
	public void init() {
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		descriptorService.getAllDescriptors();
		
		for (String beanName : beanNames) {
			Object object = applicationContext.getBean(beanName);
			if (object.getClass().getAnnotation(Entity.class) != null && object != this) {
				IClassDescriptor classDescriptor = descriptorService.getClassDescriptor(object.getClass());
				IPropertyDescriptor identifierDescriptor = classDescriptor.getIdentifierDescriptor();
				Object id = null;
				String propertyName = identifierDescriptor.getName();
				try {
					id = Ognl.getValue(propertyName, object);
				} catch (OgnlException e) {
					log.warn("Couldn't get the id of a seed bean " + object + " because of: ", e);
				}
				
				// Try to find if a persistent entity already exists based on unique property or manually set id
				ValidateUniqueness validateUniqueness = object.getClass().getAnnotation(ValidateUniqueness.class);
				if (validateUniqueness == null && id == null) {
		    	log.info("Entity of type " + object.getClass() + " doesn't have uniquely identifying property. Can't check if entity already exist. Will seed new entity: " + object);
				}
				else {
			    DetachedCriteria criteria = DetachedCriteria.forClass(object.getClass());
					if (validateUniqueness != null) {
						propertyName = validateUniqueness.property();
				    try {
							criteria.add(Restrictions.eq(propertyName, Ognl.getValue(propertyName, object) ));
						} catch (OgnlException e) {
							log.error("Couldn't find if an entity already exists because of: ", e);
						}
					}
					else criteria.add(Restrictions.eq(propertyName, id) );

					Object savedObject = persistenceService.getInstance(criteria);
				    
			    if (savedObject != null) {
		    		try {
							log.info("Entity of type " + object.getClass() + " identified by unique property " + propertyName + " " + Ognl.getValue(propertyName, savedObject) + " already exists");
						} catch (OgnlException e) {
							log.warn("Entity of type " + object.getClass() + " identified by unique property " + propertyName + " exists, but couldn't display value of identifying property because of: ", e);
						}
						
						// Need to set the ids to seed beans so a new seed entity with a relationship to existing seed entities can be saved  
						try {
							id = Ognl.getValue(identifierDescriptor.getName(), savedObject);
							Ognl.setValue(identifierDescriptor.getName(), object, id);						
						} catch (OgnlException e) {
							log.warn("Couldn't set the id of an already existing entity because of: ", e);
						}
		    		continue;
			    }
				}
	    	persistenceService.save(object);
			}
		}
	}
}
