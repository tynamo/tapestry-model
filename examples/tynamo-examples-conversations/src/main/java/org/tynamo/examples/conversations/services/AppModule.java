package org.tynamo.examples.conversations.services;

import org.apache.tapestry5.ioc.annotations.SubModule;
import org.tynamo.conversations.services.ConversationModule;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule(ConversationModule.class)
public class AppModule
{
}
