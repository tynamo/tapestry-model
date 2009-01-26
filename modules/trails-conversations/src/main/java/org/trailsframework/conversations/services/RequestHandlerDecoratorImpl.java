package org.trailsframework.conversations.services;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.services.AspectDecorator;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestParameters;

public class RequestHandlerDecoratorImpl implements RequestHandlerDecorator
{
    private final AspectDecorator aspectDecorator;

    private final ConversationManager conversationManager;

    private final Cookies cookies;

    public RequestHandlerDecoratorImpl(AspectDecorator aspectDecorator, ConversationManager conversationManager, Cookies cookies) {
        this.aspectDecorator = aspectDecorator;
        this.conversationManager = conversationManager;
        this.cookies = cookies;
    }

    public <T> T build(Class<T> serviceInterface, T delegate)
    {

        MethodAdvice advice = new MethodAdvice() {
            public void advise(Invocation invocation) {
                if (invocation.getMethodName() == "handle") {
                    Object parameterObject = invocation.getParameter(0);
                    EventContext activationContext = null;
                    String pageName = null;
                    if (parameterObject instanceof PageRenderRequestParameters) {
                        activationContext = ((PageRenderRequestParameters)parameterObject).getActivationContext();
                        pageName = ((PageRenderRequestParameters)parameterObject).getLogicalPageName();
                    }
                    else if (parameterObject instanceof ComponentEventRequestParameters) {
                        activationContext = ((ComponentEventRequestParameters) parameterObject).getPageActivationContext();
                        pageName = ((ComponentEventRequestParameters)parameterObject).getActivePageName();
                    }
                    
                    String conversationId = null;
                    
                    // Try reading the conversation id from a cookie first
                    try {
                      conversationId = cookies.readCookieValue(pageName + ConversationManagerImpl.Keys._conversationId); 
                    }
                    catch (NumberFormatException e) {
                        //Ignore
                    }
                    if (conversationId != null) conversationManager.activateConversation(conversationId);
                    // If cookie isn't available, try activation context
                    else if (activationContext != null) try {
                        conversationId = activationContext.get(String.class, activationContext.getCount()-1);
                    }
                    catch(RuntimeException e) { 
                        //Ignore
                    }
                }
                invocation.proceed();
            }
        };

        return aspectDecorator.build(serviceInterface, delegate, advice, String.format("<Conversational context interceptor for %s>", serviceInterface.getName()));
    }
}