package org.trails.demo;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.callback.ICallback;
import org.trails.TrailsRuntimeException;
import org.trails.callback.CollectionCallback;
import org.trails.callback.EditCallback;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.EditPage;
import org.trails.page.PageResolver;
import org.trails.persistence.PersistenceException;
import org.trails.persistence.PersistenceService;

/**
 * This custom page is a hybrid taken from collection editor.
 *
 * @OneToOne use case.
 *
 * EXAMPLE:
 *         one-to-one association
 *
 *                 Organization-<>-----Director
 *
 * EditLink in OrganizationEdit page will show the only director currently
 * associated to an organization. REMOVE and ADD_NEW buttons operate on the
 * director as needed.
 *
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 */

public abstract class OrganizationEdit extends EditPage {

    @InjectObject("spring:pageResolver")
    public abstract PageResolver getPageResolver();

    @InjectObject("spring:persistenceService")
    public abstract PersistenceService getPersistenceService();

    @InjectObject("spring:editorService")
    public abstract BlockFinder getBlockFinder();

    @InjectObject("spring:descriptorService")
    public abstract DescriptorService getDescriptorService();

    public void showAddPage(IRequestCycle cycle) {
        //getCallbackStack().push(buildOwnerCallback()); // tell owner to re-render

        EditPage organizationEditPage = (EditPage)getPageResolver().resolvePage(cycle,
                Organization.class.getName(),
                PageType.EDIT);

        EditPage directorEditPage = (EditPage) getPageResolver().resolvePage(cycle,
                Director.class.getName(), PageType.EDIT);

        //directorEditPage.getCallbackStack().push(buildOwnerCallback()); // tell owner to re-render
        //organizationEditPage.getCallbackStack().push(buildOwnerCallback()); // tell owner to re-render

        try {
            Object newInstance = buildNewDirectorInstance();
            EditCallback addPage = new EditCallback(directorEditPage.getPageName(),
                    newInstance);
            EditPage thisPage = (EditPage) cycle.getPage(getPage().getRequestCycle().getPage().getPageName());
            thisPage.setNextPage(addPage);

            directorEditPage.getCallbackStack().push(buildOwnerCallback()); // tell owner to re-render

            //directorEditPage.setModel(newInstance);
            //organizationEditPage.setModel(getModel());
            //getPersistenceService().reattach(newInstance);
        } catch (Exception ex) {
            throw new TrailsRuntimeException(ex);
        }
    }

    protected Object buildNewDirectorInstance() throws InstantiationException,
            IllegalAccessException {
        Object object = new Director();
        return object;
    }

    EditCallback buildOwnerCallback() {
        EditCallback callback = new EditCallback(getPage().getRequestCycle()
                .getPage().getPageName(), getModel());
        return callback;
    }

    public void remove(IRequestCycle cycle) {
        try {
            Director element = (Director) ((Organization) getModel())
                    .getDirector();
            if (element != null)
                getPersistenceService().remove(element);
        } catch (PersistenceException pe) {
            getDelegate().record(pe);
            return;
        }
        ICallback callback = getCallbackStack().popPreviousCallback();
        if (callback instanceof CollectionCallback)
            ((CollectionCallback) callback).remove(getPersistenceService(),
                    getModel());
        callback.performCallback(cycle);
    }

    public boolean isNotCurrentlyAssociatedWithDirector() {
        Organization orgModel = ((Organization) getModel());
        Director model = orgModel.getDirector();
        if (model == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCurrentlyAssociatedWithDirector() {
        return !isNotCurrentlyAssociatedWithDirector();
    }

    public String getLinkName() {
        Organization orgModel = ((Organization) getModel());
        Director model = orgModel.getDirector();

        return model.toString();
    }
}