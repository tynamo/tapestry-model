package org.trailsframework.conversations.services;

public interface ConversationManager {
    public enum Parameters {inConversation};
    
    public boolean exists(String conversationId);
    
    public String createConversation(String pageName, Integer maxIdleSeconds);

    public String createConversation(String pageName, Integer maxIdleSeconds, boolean useCookie);

    public String createConversation(String id, String pageName, Integer maxIdleSeconds, boolean useCookie);
    
    public String getActiveConversation();
    
    public int getSecondsActiveConversationBecomesIdle();
    
    public void activateConversation(String conversationId);
    
    public String endActiveConversationIfIdle();
    
    public String endConversation(String conversationId);

    public void setPagePersistentFieldStrategy(ConversationalPersistentFieldStrategy pagePersistentFieldStrategy);

}
