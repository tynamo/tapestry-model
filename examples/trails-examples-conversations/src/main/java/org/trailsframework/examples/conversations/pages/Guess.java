package org.trailsframework.examples.conversations.pages;

import java.util.Random;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Meta;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.trailsframework.conversations.services.ConversationManager;

@Meta("tapestry.persistence-strategy=conversation")
@IncludeJavaScriptLibrary("Guess.js")
public class Guess
{
  private final Random random = new Random();
  
  @Inject
  private Request request;
  
  @Inject 
  private ConversationManager conversationManager;

  @Inject 
  private ComponentResources componentResources;
  
  
  @Persist
  private int target;
  
  @Property
  private int guess;
  
  // @Persist("session")
  private String conversationId;
  
  //@Persist
  //@Property
  //private Hashtable<Long,Hashtable<String,Object>> conversations;
  
  /*
  public Object onActivate() {
      if (target == 0) target = random.nextInt(10) + 1;
      if (conversationId == null || !conversationManager.exists(conversationId)) {
          conversationId = conversationManager.createConversation(componentResources.getPageName(), 10, true);
          return null;
      }
      else return null;
  }*/
  public Object onActivate() {
      if (target == 0) return createConversation();
      else return null;
  }
  
  private Object createConversation() {
      conversationId = conversationManager.createConversation(componentResources.getPageName(), 60, false);
      return this;
  }
  
  
  public Object onActivate(String conversationId) {
      if (!conversationId.equals(conversationManager.getActiveConversation()) ) return createConversation();
      
      if (target == 0) target = random.nextInt(10) + 1;

      this.conversationId = conversationId;
      return null;
  }

  /*
  String onPassivate() {
      return null; // conversationId;
  }
  */  
  
  String onPassivate() {
      return conversationId;
  }  
  
  public int getTarget()
  {
    return target;
  }
  
  @Inject
  private Block messageBlock;

  @Inject
  @Property
  private Block gameBlock;  
  
  @Inject
  private Block gameEndBlock;

  @Property
  private String message;

  Block onActionFromGuessLink(int guess)
  {
    if (guess == target) return gameEndBlock;

    if (guess < target)
      message = String.format("%d is too low.", guess);
    else
      message = String.format("%d is too high.", guess);

    return gameBlock;
  }  

  Object onActionFromRestartLink()
  {
      conversationId = conversationManager.endConversation(conversationId);
      target = 0;
      return this;
  }
  
}