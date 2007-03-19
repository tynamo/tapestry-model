package org.trails.demo;

import net.sf.tacos.ajax.AjaxWebRequest;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.trails.page.ListPage;

public abstract class NoteListPage extends ListPage implements PageBeginRenderListener
{
    public abstract Note getNote();

    public abstract void setNote(Note Note);

    public void addNote()
    {
        addStatusResponse(getRequestCycle(), "Added: " + getNote().toString());
        getPersistenceService().save(getNote());
        setNote(new Note());
    }

    public void pageBeginRender(PageEvent arg0)
    {
        setNote(new Note());

    }

    private void addStatusResponse(IRequestCycle iRequestCycle, String messagge)
    {
        AjaxWebRequest ajax = (AjaxWebRequest) iRequestCycle.getAttribute(AjaxWebRequest.AJAX_REQUEST);
        if (ajax != null) ajax.addStatusResponse(messagge);
    }

}
