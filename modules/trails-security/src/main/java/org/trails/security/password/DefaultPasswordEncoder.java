package org.trails.security.password;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.springframework.dao.DataAccessException;

public class DefaultPasswordEncoder implements PasswordEncoder {
	public String encodePassword(String rawPass, Object salt) throws DataAccessException {		
		return DigestUtil.encode(rawPass);
	}
	
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
		return DigestUtil.equalsEncoded(encPass, rawPass);
	}
}
