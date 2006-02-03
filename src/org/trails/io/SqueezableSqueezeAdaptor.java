package org.trails.io;

import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class SqueezableSqueezeAdaptor extends HibernateDaoSupport implements SqueezeAdaptor {
	private static final String PREFIX = "C";

	public String getPrefix() {
		return PREFIX;
	}

	public Class getDataClass() {
		return Squeezable.class;
	}

	@Transactional(readOnly = true)
    public String squeeze( DataSqueezer squeezer, Object data )
    {
        final String squeezed =
                squeezer.squeeze( new ObjectIdentity( getSession().getEntityName( data ), getSession().getIdentifier( data ) ) );
        return PREFIX + squeezed;
    }

    @Transactional(readOnly = true)
    public Object unsqueeze( DataSqueezer squeezer, String string )
    {
        final String squeezed = string.substring( PREFIX.length() );
        final ObjectIdentity identity = ( ObjectIdentity ) squeezer.unsqueeze( squeezed );
        return getSession().get( identity.getEntityName(), identity.getId() );
    }

}
