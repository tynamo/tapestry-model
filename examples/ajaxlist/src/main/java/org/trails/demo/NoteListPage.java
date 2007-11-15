package org.trails.demo;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.page.HibernateListPage;

public abstract class NoteListPage extends HibernateListPage implements PageBeginRenderListener
{
	public abstract Note getNote();

	public abstract void setNote(Note Note);

	/**
	 * HibernateListPage doens't persist the "criteria" by default. If you need to rewind the page, like in an AJAX
	 * page, you need to explicitly tell the page to persist the property.
	 */
	@Persist
	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);


	public void addNote()
	{
		addStatusResponse(getRequestCycle(), "Added: " + getNote().toString());
		getPersistenceService().save(getNote());
		setNote(new Note());
	}

	public void pageBeginRender(PageEvent event)
	{
//		super.pageBeginRender(event);
		setNote(new Note());
	}

	private void addStatusResponse(IRequestCycle iRequestCycle, String messagge)
	{
/*		AjaxWebRequest ajax = (AjaxWebRequest) iRequestCycle.getAttribute(AjaxWebRequest.AJAX_REQUEST);
		if (ajax != null) ajax.addStatusResponse(messagge);
*/
	}

}
