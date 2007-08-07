package org.trails.security;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.ui.rememberme.RememberMeServices;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.persistence.HibernatePersistenceService;

public class RollingCookieRememberMeServices implements RememberMeServices {
	private static final Log log = LogFactory.getLog(RollingCookieRememberMeServices.class );
	
	private static Random random = new Random((new Date()).getTime() ); 
	private enum Keys{j_rememberme, remembermetoken}
	HibernatePersistenceService persistenceService;
	
	private char separatorChar = '-';
	// In seconds, default is a month
	private int maxAge = 30 * 24 * 3600;

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0)) return null;
		for (Cookie cookie : cookies) if (Keys.remembermetoken.name().equals(cookie.getName()) ) {
			String cookieValue = cookie.getValue();
			int separatorPos = cookieValue.indexOf(separatorChar );
			if (separatorPos <= 0) return null;
			//if (!cookie.getPath().equals(request.getContextPath())) return null;
			log.info("Trying to remember user from " + request.getRemoteAddr() + " with credentials " + cookieValue);
			return new UserKeyAuthenticationToken(cookieValue.substring(separatorPos+1), cookieValue.substring(0, separatorPos) );
		}
		return null;
	}

	public void loginFail(HttpServletRequest request, HttpServletResponse response) {
		clearRememberMeCookie(request.getContextPath(), response);
	}
	
	private String createExpiringKeyForUser(String username) {
		// purge expired tokens here so we don't need to do it periodically - ok to do this everytime?
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExpiringKey.class);
			detachedCriteria.add(Restrictions.eq("name", username) );
			detachedCriteria.add(Restrictions.lt("expiresAfter", new Date()) );
			List<ExpiringKey> credentials = persistenceService.getInstances(ExpiringKey.class, detachedCriteria );
			persistenceService.removeAll(credentials);
		}
		catch (Exception e) {
			log.warn("Purging expired credentials failed because of: " + e.getMessage() );
		}

		ExpiringKey expiringKey = new ExpiringKey(username, (new BigInteger(128, random)).toString(), new Date((new Date()).getTime() + maxAge * 1000L) );
		persistenceService.save(expiringKey);
		return expiringKey.getValue() + separatorChar + expiringKey.getName();
	}
	
	public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if (log.isTraceEnabled()) log.trace("j_rememberme is " + request.getParameter(Keys.j_rememberme.name()) );
		
		if (request.getParameter(Keys.j_rememberme.name()) == null && !(authentication instanceof UserKeyAuthenticationToken) ) return; 
		
		if (authentication instanceof UserKeyAuthenticationToken) try {
			// Rolling tokens, remove the used one
			// TODO This performs slowly, would be better to add a HQL pass-through method in persistence service
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExpiringKey.class);
			detachedCriteria.add(Restrictions.eq("name", authentication.getName() ) );
			detachedCriteria.add(Restrictions.eq("value", authentication.getCredentials() ) );
			List<ExpiringKey> credentials = persistenceService.getInstances(ExpiringKey.class, detachedCriteria );
			
			// persistenceService.removeAll(credentials) won't work. When remember me is used, there may be several incoming requests that are sending the same
			// token as credentials. If we invalidate the token after the first request, the second request fails, the request
			// is redirected to the the login page, but that request is authenticated with the new cookie and so we go to
			// an infinite loop. Instead, make the credentials expire soon (for example less than session timeout)
			// Assume there's only one credential in the list. Expired credentials will be cleaned up later in any case
			if (credentials.size() > 0) {
				ExpiringKey credential = credentials.get(0); 
				// Expire in one min
				credential.setExpiresAfter(new Date((new Date()).getTime() + 60000L));
				persistenceService.save(credential);
			}
		}
		catch (Exception e) {
			log.warn("Couldn't expire used credentials because of " + e.getMessage() );
		}
		
		String username = authentication.getName();
		Cookie cookie = new Cookie(Keys.remembermetoken.name(), createExpiringKeyForUser(username).toString() );
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	public void setPersistenceService(HibernatePersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}
	
	public char getSeparatorChar() {
		return separatorChar;
	}

	public void setSeparatorChar(char separatorChar) {
		this.separatorChar = separatorChar;
	}
	
	public static void clearRememberMeCookie(String contextPath, HttpServletResponse response) {
		Cookie cookie = new Cookie(Keys.remembermetoken.name(), "");
		cookie.setPath(contextPath == null ? "/" : contextPath);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
