package org.trails.descriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.component.AssociationMgt;
import org.trails.component.AssociationSelect;
import org.trails.hibernate.HibernateDescriptorDecorator;

/**
 * This class represents a one-to-one association and is created by
 * HibernateDescriptorDecorator at bootstrap time
 *
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 * @OneToOne use case.
 * <p/>
 * EXAMPLE: one-to-one association
 * <p/>
 * Organization-<>-----Director
 * <p/>
 * Organization is the owner. Director is the reference.
 * <p/>
 * Each requires a specialized trails property editor.
 * <p/>
 * Both of these objects are manipulated by dedicated descriptors respectively
 * OwningObjectReferenceDescriptor and ObjectReferenceDescriptor which have a
 * sole duty of governing which property editor launches.
 * <p/>
 * In short this guy operates the owning side of the association for the
 * framework and gets detected as owner iff OneToOne does NOT have the mappedBy
 * attribute set
 * @see HibernateDescriptorDecorator
 * @see ObjectReferenceDescriptor
 * @see AssociationSelect
 * @see AssociationMgt
 */
public class OwningObjectReferenceDescriptor implements IDescriptorExtension
{

	protected static final Log LOG = LogFactory.getLog(OwningObjectReferenceDescriptor.class);

	private String inverseProperty = null;

	public String getInverseProperty()
	{
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty)
	{
		this.inverseProperty = inverseProperty;
	}

}
