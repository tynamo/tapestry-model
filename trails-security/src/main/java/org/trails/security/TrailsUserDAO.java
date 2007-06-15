/**
 * $Author: alejandroscandroli $
 * $Id: TrailsUserDAO.java,v 1.1 2006/01/16 11:43:38 alejandroscandroli Exp $
 */

package org.trails.security;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.persistence.PersistenceService;


public class TrailsUserDAO implements UserDetailsService
{

	HibernatePersistenceService persistenceService;

	public void setPersistenceService(HibernatePersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(UserDetails.class);
		criteria.add(Restrictions.eq("username", username));
		return persistenceService.getInstance(UserDetails.class, criteria);
	}
}
