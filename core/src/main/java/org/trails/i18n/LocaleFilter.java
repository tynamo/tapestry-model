package org.trails.i18n;

import java.io.IOException;

import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.services.WebRequestServicerFilter;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * A Tapestry request pipeline contribution whose job it is
 * to pass the threadLocale into Spring so the aspect can see it
 * @author cnelson
 *
 */
public class LocaleFilter implements WebRequestServicerFilter
{
    private LocaleHolder localeHolder;
    
    private ThreadLocale threadLocale;
    
    public void service(WebRequest request, WebResponse response, WebRequestServicer servicer) throws IOException
    {
        getLocaleHolder().setThreadLocale(threadLocale);
        servicer.service(request, response);
    }
    
    public LocaleHolder getLocaleHolder()
    {
        return localeHolder;
    }

    public void setLocaleHolder(LocaleHolder localeHolder)
    {
        this.localeHolder = localeHolder;
    }

    public ThreadLocale getThreadLocale()
    {
        return threadLocale;
    }

    public void setThreadLocale(ThreadLocale threadLocale)
    {
        this.threadLocale = threadLocale;
    }

}
