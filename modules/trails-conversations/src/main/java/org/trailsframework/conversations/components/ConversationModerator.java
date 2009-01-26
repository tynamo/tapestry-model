package org.trailsframework.conversations.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.trailsframework.conversations.services.ConversationManager;

@IncludeJavaScriptLibrary("ConversationModerator.js")
public class ConversationModerator {
    
    @Persist
    @Property
    private boolean active;
    
    @Persist("conversation")
    @Property
    private int i;

    @Inject
    private ComponentResources componentResources;
    
    @Environmental
    private RenderSupport renderSupport;
    
    JSONObject onAction() {
        active = conversationManager.getActiveConversation() == null ? false : true;
        i++;
        JSONObject object = new JSONObject();
        object.put("content", i);
        return object;
    }

    JSONObject onCheckEnd() {
        JSONObject object = new JSONObject();
        object.put("newTimeout", conversationManager.getSecondsActiveConversationBecomesIdle() );
        conversationManager.endActiveConversationIfIdle();
        return object;
    }

    public int getGameId() {
            return i;
    }
    
    @Inject 
    private ConversationManager conversationManager;
    
    public String getUpdateURI()
    {
        // TODO hhmmm don't see any difference whatever event type I use - it always calls "action"
        Link link = componentResources.createEventLink("checkEnd");
        String absoluteURI = link.toAbsoluteURI();
        absoluteURI = absoluteURI.contains("?") ? absoluteURI + "&" : absoluteURI + "?";
        return absoluteURI + ConversationManager.Parameters.inConversation.name() + "=false";
    }
    
    @AfterRender
    public void afterRender() {
        System.out.println("Active conversation is " + conversationManager.getActiveConversation());
            renderSupport.addScript(String.format("var conversationModerator = new ConversationModerator(null, '%s', 15, null, null);", getUpdateURI() ));
    }
    

}
