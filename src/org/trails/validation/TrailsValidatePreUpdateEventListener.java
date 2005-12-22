/*
 * Created on 29/11/2005
 *
 */
package org.trails.validation;

import org.hibernate.event.PreUpdateEvent;
import org.hibernate.validator.event.ValidatePreUpdateEventListener;

public class TrailsValidatePreUpdateEventListener extends
		ValidatePreUpdateEventListener {

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		HibernateClassValidatorFactory.getSingleton().validateEntity(event.getEntity());
		return super.onPreUpdate(event);
	}
	
}
