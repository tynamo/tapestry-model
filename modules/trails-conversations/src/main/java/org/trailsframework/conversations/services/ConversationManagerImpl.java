package org.trailsframework.conversations.services;

import java.util.HashMap;
import java.util.Map;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;

public class ConversationManagerImpl implements ConversationManager {
    // protected so you can override toString() in case user application already uses the same keys
    // for something else
    protected enum Keys {_conversationId, conversations};

    private final Request request;
    private final Cookies cookies;
    private ConversationalPersistentFieldStrategy pagePersistentFieldStrategy;
    
    public ConversationManagerImpl(Request request, Cookies cookies) {
        this.request = request;
        this.cookies = cookies;
    }
    
    @SuppressWarnings("unchecked")
    protected Map<String,Conversation> getConversations() {
        Map<String,Conversation> conversations = (Map<String,Conversation>)request.getSession(true).getAttribute(Keys.conversations.toString());
        if (conversations == null) {
            conversations = new HashMap<String,Conversation>();
            request.getSession(true).setAttribute(Keys.conversations.toString(), conversations);
        }
        return conversations;
    }

    public void activateConversation(String conversationId) {
        if (exists(conversationId)) request.setAttribute(Keys._conversationId.toString(), conversationId);
    }

    public String createConversation(String pageName, Integer maxIdleSeconds) {
        return createConversation(pageName, maxIdleSeconds, false);
        
    }
    public String createConversation(String pageName, Integer maxIdleSeconds, boolean useCookie) {
        return createConversation(String.valueOf(System.currentTimeMillis()), pageName, maxIdleSeconds, useCookie);
    }
    
    public String createConversation(String id, String pageName, Integer maxIdleSeconds, boolean useCookie) {
        pageName = pageName == null ? "" : pageName.toLowerCase();
        // Don't use path in a cookie, it's actually relatively difficult to find out from here
        if (useCookie) cookies.writeCookieValue(pageName + Keys._conversationId.toString(), String.valueOf(id));
        getConversations().put(id,new Conversation(id,pageName, maxIdleSeconds, useCookie));
        endIdleConversations();
        activateConversation(id);
        return id;
    }
    
    
    public void endIdleConversations() {
        for (Conversation conversation : getConversations().values()) if (conversation.isIdle(false)) endConversation(conversation.getId()); 
    }

    public String endConversation(String conversationId) {
        Conversation conversation = getConversations().get(conversationId);
        if (conversation == null) return null;
        if (conversation.isUsingCookie()) cookies.removeCookieValue(String.valueOf(conversationId) );
        // TODO We are not getting any strategies here because they have not been contributed to this service
        if (pagePersistentFieldStrategy != null) pagePersistentFieldStrategy.discardChanges(conversation.getPageName());
        getConversations().remove(conversationId);
        return null;
    }

    public boolean exists(String conversationId) {
        Conversation conversation = getConversations().get(conversationId);
        if (conversation == null) return false;
        else return true;
    }
    
    public String endActiveConversationIfIdle() {
        String conversationId = getActiveConversation();
        Conversation conversation = getConversations().get(conversationId);
        if (conversation == null) return null;
        boolean resetTimeout = !("false".equals(request.getParameter(Parameters.inConversation.name()))); 
        if (conversation.isIdle(resetTimeout)) return endConversation(conversationId);
        return conversationId;
    }

    public String getActiveConversation() {
        String conversationId = (String)request.getAttribute(Keys._conversationId.toString());
        if (conversationId == null) return null;
        return exists(conversationId) ? conversationId : null;
    }
    
    public int getSecondsActiveConversationBecomesIdle() {
        String conversationId = getActiveConversation();
        if (conversationId == null) return -1;
        Conversation conversation = getConversations().get(conversationId);
        if (conversation == null) return -1;
        return conversation.getSecondsBecomesIdle();
    }

    public void setPagePersistentFieldStrategy(ConversationalPersistentFieldStrategy pagePersistentFieldStrategy) {
        this.pagePersistentFieldStrategy = pagePersistentFieldStrategy;
    }
}
