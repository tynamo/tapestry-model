package org.trailsframework.conversations.services;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.apache.tapestry5.ioc.internal.util.CollectionFactory.newList;
import static org.apache.tapestry5.ioc.internal.util.Defense.notBlank;
import org.apache.tapestry5.internal.services.PersistentFieldChangeImpl;
import org.apache.tapestry5.services.PersistentFieldChange;
import org.apache.tapestry5.services.PersistentFieldStrategy;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

public class ConversationalPersistentFieldStrategy implements PersistentFieldStrategy
{
    /**
     * Prefix used to identify keys stored in the session that are being used to store persistent field data.
     */
    static final String PREFIX = "state:";
    private final ConversationManager conversationManager;

    public ConversationalPersistentFieldStrategy(Request request, ConversationManager conversationManager)
    {
        this.prefix = PREFIX;
        this.request = request;
        conversationManager.setPagePersistentFieldStrategy(this);
        this.conversationManager = conversationManager;
    }
    
    private final String prefix;

    private final Request request;
    
    private String buildPrefix(String pageName) {
        //System.out.println("Conversation id is: " + conversationManager.getActiveConversation());
        return prefix + pageName + "_" + "conversation" + conversationManager.getActiveConversation() + ":"; 
    }

    public final Collection<PersistentFieldChange> gatherFieldChanges(String pageName)
    {
        Session session = request.getSession(false);

        if (session == null) return Collections.emptyList();

        List<PersistentFieldChange> result = newList();
        
        // if conversation is not active don't gather any changes
        if (conversationManager.getActiveConversation() == null) return result;

        String fullPrefix = buildPrefix(pageName);

        for (String name : session.getAttributeNames(fullPrefix))
        {
            Object persistedValue = session.getAttribute(name);

            Object applicationValue = persistedValue == null ? null : convertPersistedToApplicationValue(
                    persistedValue);

            PersistentFieldChange change = buildChange(name, applicationValue);

            result.add(change);

            didReadChange(session, name);
        }

        return result;
    }

    public void discardChanges(String pageName)
    {
        Session session = request.getSession(false);

        if (session == null || conversationManager.getActiveConversation() == null) return;
        
        String fullPrefix = buildPrefix(pageName);

        for (String name : session.getAttributeNames(fullPrefix))
        {
            session.setAttribute(name, null);
        }
    }

    /**
     * Called after each key is read by {@link #gatherFieldChanges(String)}. This implementation does nothing,
     * subclasses may override.
     *
     * @param session       the session from which a value was just read
     * @param attributeName the name of the attribute used to read a value
     */
    protected void didReadChange(Session session, String attributeName)
    {
    }

    private PersistentFieldChange buildChange(String name, Object newValue)
    {
        String[] chunks = name.split(":");

        // Will be empty string for the root component
        String componentId = chunks[2];
        String fieldName = chunks[3];

        return new PersistentFieldChangeImpl(componentId, fieldName, newValue);
    }

    public final void postChange(String pageName, String componentId, String fieldName,
                                 Object newValue)
    {
        notBlank(pageName, "pageName");
        notBlank(fieldName, "fieldName");
        
        // If no active conversation, no changes to post
        if (conversationManager.getActiveConversation() == null) return;

        Object persistedValue = newValue == null ? null : convertApplicationValueToPersisted(newValue);

        StringBuilder builder = new StringBuilder(buildPrefix(pageName) );

        if (componentId != null) builder.append(componentId);

        builder.append(':');
        builder.append(fieldName);

        Session session = request.getSession(persistedValue != null);

        // TAPESTRY-2308: The session will be false when newValue is null and the session
        // does not already exist.

        if (session != null)
        {
            session.setAttribute(builder.toString(), persistedValue);
        }
    }

    /**
     * Hook that allows a value to be converted as it is written to the session. Passed the new value provided by the
     * application, returns the object to be stored in the session. This implementation simply returns the provided
     * value.
     *
     * @param newValue non-null value
     * @return persisted value
     * @see #convertPersistedToApplicationValue(Object)
     */
    protected Object convertApplicationValueToPersisted(Object newValue)
    {
        return newValue;
    }

    /**
     * Converts a persisted value stored in the session back into an application value.   This implementation returns
     * the persisted value as is.
     *
     * @param persistedValue non-null persisted value
     * @return application value
     * @see #convertPersistedToApplicationValue(Object)
     */
    protected Object convertPersistedToApplicationValue(Object persistedValue)
    {
        return persistedValue;
    }
}
