package org.trails.io;

import java.io.Serializable;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.persistence.PersistenceService;

public class SqueezableSqueezeAdaptor  implements SqueezeAdaptor {
	public static final String PREFIX = "C";

	public String getPrefix() {
		return PREFIX;
	}

	public Class getDataClass() {
		return Squeezable.class;
	}

	private DescriptorService descriptorService;
	
	private PersistenceService persistenceService;
	
    public String squeeze( DataSqueezer squeezer, Object data )
    {
    	Serializable id = null;
    	try
    	{
    		IClassDescriptor classDescriptor = getDescriptorService().getClassDescriptor(data.getClass());
    		id = (Serializable)Ognl.getValue(classDescriptor.getIdentifierDescriptor().getName(), data);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        final String squeezed =
                squeezer.squeeze( new ObjectIdentity( data.getClass().getName(),  
                id) );
        
        return PREFIX + squeezed;
    }

    public Object unsqueeze( DataSqueezer squeezer, String string )
    {
        final String squeezed = string.substring( PREFIX.length() );
        final ObjectIdentity identity = ( ObjectIdentity ) squeezer.unsqueeze( squeezed );
        try
        {
        	return getPersistenceService().getInstance( Class.forName(identity.getEntityName()), identity.getId() );
        }
        catch(ClassNotFoundException ce)
        {
        	// TODO Fix this to log
        	ce.printStackTrace();
        	return null;
        }
    }

	public DescriptorService getDescriptorService()
	{
		return descriptorService;
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

}
