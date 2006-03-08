/**
 * $Author: alejandroscandroli $
 * $Id: TrailsUserDAO.java,v 1.1 2006/01/16 11:43:38 alejandroscandroli Exp $
 */

package org.trails.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;
import org.trails.persistence.PersistenceService;
import org.trails.security.domain.Role;


public class TrailsUserDAO implements UserDetailsService
{

    PersistenceService persistenceService;

    public PersistenceService getPersistenceService()
    {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService)
    {
        this.persistenceService = persistenceService;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
    {
        org.trails.security.domain.User example = new org.trails.security.domain.User();
        example.setUsername(username);

        List users = getPersistenceService().getInstances(example);

        if (users.size() == 0)
        {
            throw new UsernameNotFoundException("User not found");
        }

        org.trails.security.domain.User user = (org.trails.security.domain.User) users.get(0);

        Set<Role> roles = user.getRoles();

        if (roles.size() == 0)
        {
            throw new UsernameNotFoundException("User has no GrantedAuthority");
        }

        List<GrantedAuthority> listAuth = new ArrayList<GrantedAuthority>();

        for (Role role : roles)
        {
            listAuth.add(new GrantedAuthorityImpl(role.getName()));
        }

        GrantedAuthority[] arrayAuths = new GrantedAuthority[listAuth.size()];
        arrayAuths = listAuth.toArray(arrayAuths);

        return new org.acegisecurity.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), !user.getAccountExpired(), !user.getCredentialsExpired(), !user.getAccountLocked(), arrayAuths);
    }
}
