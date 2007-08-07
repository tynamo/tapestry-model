package org.trails.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.html.BasePage;

public abstract class LoginPage extends BasePage {

	private static final Log LOG = LogFactory.getLog(LoginPage.class);

	public abstract String getUsername();

	public abstract String getPassword();

	public abstract boolean getRememberMe();
	
	public void login(IRequestCycle cycle) throws RedirectException {

		LOG.debug("User " + getUsername() + " is attempting login.");

		String acegiUrl = cycle.getAbsoluteURL("/j_acegi_security_check?j_username=" + getUsername() + "&j_password=" + getPassword() + "&j_rememberme=" + getRememberMe());

		throw new RedirectException(acegiUrl);
	}

}
