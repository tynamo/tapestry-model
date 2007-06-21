package org.trails.demo.service;

import org.acegisecurity.userdetails.UserDetails;
import org.apache.tapestry.engine.state.StateObjectFactory;
import org.apache.tapestry.web.WebRequest;
import org.trails.security.TrailsUserDAO;

public class CurrentUserFactory implements StateObjectFactory {

	private WebRequest webRequest;
	private TrailsUserDAO userDao;

	public Object createStateObject() {
		if (webRequest.getRemoteUser() == null) return null;

		return (UserDetails)userDao.loadUserByUsername(webRequest.getRemoteUser());
	}

  public void setWebRequest(WebRequest webRequest) {
  	this.webRequest = webRequest;
  }
	public void setUserDao(TrailsUserDAO userDao) {
		this.userDao = userDao;
	}
}
