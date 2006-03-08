/*
 * Created on 29/11/2005
 *
 */
package org.trails.validation;

import org.hibernate.event.PreInsertEvent;
import org.hibernate.validator.event.ValidatePreInsertEventListener;

public class TrailsValidatePreInsertEventListener extends ValidatePreInsertEventListener {

	private static final long serialVersionUID = 1L;

	public boolean onPreInsert(PreInsertEvent event) {
		HibernateClassValidatorFactory.getSingleton().validateEntity(event.getEntity());
		return super.onPreInsert(event);
	}	
}
