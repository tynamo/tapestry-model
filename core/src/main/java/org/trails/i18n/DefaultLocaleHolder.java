package org.trails.i18n;

import java.io.IOException;
import java.util.Locale;

import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.services.WebRequestServicerFilter;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

public class DefaultLocaleHolder implements LocaleHolder
{
    private ThreadLocale threadLocale;
    
    public Locale getLocale()
    {
        return getThreadLocale().getLocale();
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
