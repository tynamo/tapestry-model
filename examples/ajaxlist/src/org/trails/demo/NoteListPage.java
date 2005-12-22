package org.trails.demo;

import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.trails.page.ListPage;

public abstract class NoteListPage extends ListPage implements PageBeginRenderListener
{
    public abstract Note getNote();

    public abstract void setNote(Note Note);

    public NoteListPage()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public void addNote()
    {
        getPersistenceService().save(getNote());
        setNote(new Note());
        loadInstances();
        
    }

    public void pageBeginRender(PageEvent arg0)
    {
        setNote(new Note());
        
    }
    
    
}
