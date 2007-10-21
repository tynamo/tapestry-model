package org.trails.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.persistence.HibernatePersistenceService;

public class LogoutService implements IEngineService {
	private static final Logger log = Logger.getLogger(LogoutService.class);
	
	private HibernatePersistenceService persistenceService;
	private IEngineService restartService;
	private HttpServletResponse response;

	private LinkFactory linkFactory;
	
	public ILink getLink(boolean post, Object parameter) {
		return linkFactory.constructLink(this, post, new HashMap(), false);
	}

	public void service(IRequestCycle cycle) throws IOException {
		String username = cycle.getInfrastructure().getRequest().getRemoteUser();
		if (username != null)
		{
			Cookie cookie = new Cookie("remembermetoken", "");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);

			// Hmm.. now this requires two queries, is there any way to delete all with criteria api
			// without obtaining a collection?
			try
			{
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExpiringKey.class);
				detachedCriteria.add(Restrictions.eq("name", username));
				List<ExpiringKey> credentials = persistenceService.getInstances(ExpiringKey.class, detachedCriteria);
				if (credentials.size() > 0) persistenceService.removeAll(credentials);
			}
			catch (Exception e)
			{
				log.warn("Couldn't clean up persistent credentials because of: " + e.getMessage());
			}
		}
			
		restartService.service(cycle);
	}
	
	public void setLinkFactory(LinkFactory factory) {
		linkFactory = factory;
	}
	
	public void setPersistenceService(HibernatePersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setRestartService(IEngineService restartService) {
		this.restartService = restartService;
	}
	
	public String getName() {
		return "logout";
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
