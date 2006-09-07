package org.trails.seeddata;


import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.hibernate.HibernatePersistenceService;
import org.trails.persistence.PersistenceService;

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
	 * @see com.movlet.domain.SeedDataInitializer#init()
	 */
	public void init() {
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		descriptorService.getAllDescriptors();
		
		for (String beanName : beanNames) {
			Object object = applicationContext.getBean(beanName);
			if (object.getClass().getAnnotation(Entity.class) != null && object != this) {
				// If persistenceService uses save or update, Hibernate manipulates the object directly,
				// but if it uses merge, it's safer to use the returned object
				// It's much faster to just save() but if it's not our implementation, better be safe than sorry
				//FIXME Some descriptor changes caused descriptorService.getClassDescriptor() to return nulls for
				// valid entities. The check doesn't work either way, because you get back a proxy of the persistenceService
				// For now, assume that underlying implementation calls save and figure out a better way meanwhile
				persistenceService.save(object);
				/*
				if (persistenceService instanceof HibernatePersistenceService) persistenceService.save(object);
				else saveAndSetId(object);
				*/
			}
		}
	}

	private void saveAndSetId(Object object) {
		IClassDescriptor classDescriptor = descriptorService.getClassDescriptor(object.getClass());
		IPropertyDescriptor identifierDescriptor = classDescriptor.getIdentifierDescriptor();
		Object savedObject = persistenceService.save(object);
		try {
			Object id = Ognl.getValue(identifierDescriptor.getName(), savedObject);
			Ognl.setValue(identifierDescriptor.getName(), object, id);
		} catch (OgnlException e) {
			log.fatal("Couldn't set the entity id because of: ", e); 
		}
	}
}
