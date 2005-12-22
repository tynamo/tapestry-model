package org.trails.component;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.jmock.cglib.Mock;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.persistence.PersistenceService;
import org.trails.test.Foo;

public class AssociationSelectTest extends ComponentTest
{

    public AssociationSelectTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void testBuildSelectionModel()
    {
        Mock psvcMock = new Mock(PersistenceService.class);
        List instances = new ArrayList();
        TrailsClassDescriptor classDescriptor = new TrailsClassDescriptor(Foo.class);
        classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, "id", Integer.class));
        psvcMock.expects(atLeastOnce()).method("getInstances").with(isA(DetachedCriteria.class)).will(returnValue(instances));
        AssociationSelect associationSelect = (AssociationSelect)creator.newInstance(AssociationSelect.class, 
                new Object[] {"persistenceService", psvcMock.proxy()});
        IPropertyDescriptor associationDescriptor = new TrailsPropertyDescriptor(Foo.class, "foo", Foo.class);
        associationSelect.setClassDescriptor(classDescriptor);
        associationSelect.setPropertyDescriptor(associationDescriptor);
        associationSelect.buildSelectionModel();
        IdentifierSelectionModel selectionModel = (IdentifierSelectionModel)associationSelect.getPropertySelectionModel();
        assertEquals(1, selectionModel.getOptionCount());
        associationDescriptor.setRequired(true);
        
        associationSelect.buildSelectionModel();
        selectionModel = (IdentifierSelectionModel)associationSelect.getPropertySelectionModel();
        assertEquals(0, selectionModel.getOptionCount());
    }
}
