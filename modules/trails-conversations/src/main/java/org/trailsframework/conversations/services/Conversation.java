package org.trailsframework.conversations.services;

public class Conversation {
    private final String pageName;
    private final String id;
    private final boolean usingCookie;
    private final long maxIdleSeconds;
    private long lastTouched;

    public boolean isUsingCookie() {
        return usingCookie;
    }

    protected Conversation(String id, String pageName, int maxIdleSeconds, boolean usingCookie) {
        this.id = id;
        this.pageName = pageName;
        this.usingCookie = usingCookie;
        this.maxIdleSeconds = maxIdleSeconds;
        touch();
    }
    
    public String getId() {
        return id;
    }
    public String getPageName() {
        return pageName;
    }
    
    public void touch() {
        lastTouched = System.currentTimeMillis();
    }
    
    public Integer getSecondsBecomesIdle() {
        if (maxIdleSeconds <= 0) return null;
        int i = (int)(maxIdleSeconds - (System.currentTimeMillis() - lastTouched) / 1000L);
        // If positive, crudely round up so it's surely idle after this time has passed
        // Also, don't return 0, because you might cause an infinite loop
        if (i >= 0) i++;
        System.out.println("seconds before becomes idle: " + i);
        return i;
    }

    /**
     * True if conversation has been idle for too long, otherwise resets the idletime if resetIdle is true
     **/
    public boolean isIdle(boolean resetIdle) {
        if (maxIdleSeconds < 1) {
            if (resetIdle) touch();
            return false;
        }
        if ((System.currentTimeMillis() - lastTouched) / 1000L > maxIdleSeconds) return true;
        if (resetIdle) touch();
        return false;
    }

}
