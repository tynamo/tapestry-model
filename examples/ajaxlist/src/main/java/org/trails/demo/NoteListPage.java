package org.trails.demo;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.trails.page.HibernateListPage;

public abstract class NoteListPage extends HibernateListPage implements PageBeginRenderListener
{
	public abstract Note getNote();

	public abstract void setNote(Note Note);

	public void addNote()
	{
		addStatusResponse(getRequestCycle(), "Added: " + getNote().toString());
		getPersistenceService().save(getNote());
		setNote(new Note());
	}

	public void pageBeginRender(PageEvent event)
	{
		super.pageBeginRender(event);
		setNote(new Note());
	}

	private void addStatusResponse(IRequestCycle iRequestCycle, String messagge)
	{
/*		AjaxWebRequest ajax = (AjaxWebRequest) iRequestCycle.getAttribute(AjaxWebRequest.AJAX_REQUEST);
		if (ajax != null) ajax.addStatusResponse(messagge);
*/
	}

}
