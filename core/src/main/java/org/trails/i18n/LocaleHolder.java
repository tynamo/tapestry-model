package org.trails.i18n;

import java.util.Locale;

import org.apache.hivemind.service.ThreadLocale;

public interface LocaleHolder
{
    public Locale getLocale();
    
    public void setThreadLocale(ThreadLocale threadLocale);
    
}
