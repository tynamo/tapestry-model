package org.trails.callback;

import ognl.Ognl;
import ognl.OgnlException;

import org.trails.TrailsRuntimeException;
import org.trails.descriptor.OwningObjectReferenceDescriptor;
import org.trails.page.EditPage;
import org.trails.persistence.PersistenceService;

/**
 * This guy is responsible for returning from an add or remove on a association.
 */
public class AssociationCallback extends EditCallback {
    private OwningObjectReferenceDescriptor od;

    private boolean oneToOne;

    /**
     * @param pageName
     * @param model
     */
    public AssociationCallback(String pageName, Object model,
            OwningObjectReferenceDescriptor od) {
        super(pageName, model);
        this.od = od;
        this.setOneToOne(od.isOneToOne());
    }

    public AssociationCallback(String pageName, Object model, boolean modelNew,
            EditPage nextPage) {
        super(pageName, model, modelNew);
        this.setOneToOne(od.isOneToOne());
    }

    public void save(PersistenceService persistenceService, Object newObject) {
        executeOgnlExpression(od.findAddExpression(), newObject);
        persistenceService.merge(getModel());
    }

    public void remove(PersistenceService persistenceService, Object object) {
        try {
            Ognl.setValue(od.findAddExpression(), model, null);
            Ognl.getValue(od.findAddExpression(), model);

            persistenceService.merge(getModel());
            persistenceService.remove(object);
        } catch (OgnlException e) {
            throw new TrailsRuntimeException(e);
        }
    }

    /**
     * @param previousModel
     */
    private void executeOgnlExpression(String ognlExpression, Object newObject) {

        try {
            Ognl.setValue(ognlExpression, model, newObject);
            Ognl.getValue(ognlExpression, model);
        } catch (OgnlException e) {
            throw new TrailsRuntimeException(e);
        }
    }

    public boolean isOneToOne() {
        return oneToOne;
    }

    public void setOneToOne(boolean oneToOne) {
        this.oneToOne = oneToOne;
    }
}
