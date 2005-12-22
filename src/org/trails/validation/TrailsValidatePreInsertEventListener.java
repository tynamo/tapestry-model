/*
 * Created on 29/11/2005
 *
 */
package org.trails.validation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.tapestry.services.RequestLocaleManager;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.event.ValidatePreInsertEventListener;
import org.trails.component.Utils;
import org.trails.servlet.TrailsApplicationServlet;

public class TrailsValidatePreInsertEventListener extends ValidatePreInsertEventListener {

	private static final long serialVersionUID = 1L;

	public boolean onPreInsert(PreInsertEvent event) {
		HibernateClassValidatorFactory.getSingleton().validateEntity(event.getEntity());
		return super.onPreInsert(event);
	}	
}
