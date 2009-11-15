package org.tynamo.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.FlashMessage;

/**
 * Displays message based on the message parameter
 *
 * Based on code from tapestry5-appfuse http://code.google.com/p/tapestry5-appfuse/ by Serge Eby
 *
 * @author Serge Eby
 */
public class Flash
{

	@Property
	@Persist(PersistenceConstants.FLASH)
	private FlashMessage message;

	@Inject
	private Messages messages;

	@Parameter(value = "asset:icon_success_sml.gif")
	@Property(write = false)
	private Asset iconInformation;

	@Parameter(value = "asset:icon_warning_sml.gif")
	@Property(write = false)
	private Asset iconWarning;

	final boolean beginRender(MarkupWriter writer)
	{
		// Skip if no empty of null message
		if (message == null || "".equals(message.getMessage()))
		{
			return false;
		}

		// Default to success values
		String clientId = "successMessages";
		String className = "message";
		Asset icon = iconInformation;
		String altName = messages.get("icon.information");

		// Overwrite default values if type is failure
		if (message.getType() == FlashMessage.MessageType.FAILURE)
		{
			clientId = "errorMessages";
			className = "error";
			altName = messages.get("icon.warning");
			icon = iconWarning;
		}

		writer.element("div", "id", clientId, "class", className);
		writer.element("img", "src", icon.toClientURL(), "alt", altName, "class", "icon");
		writer.write(message.getMessage());
		writer.end(); // img

		writer.end(); // div

		return false;
	}

	public void addFlashByKey(String key, FlashMessage.MessageType type, Object... args)
	{
		this.message = new FlashMessage(messages.format(key, args), type);
	}

	public void addFlash(String message, FlashMessage.MessageType type)
	{
		this.message = new FlashMessage(message, type);
	}
}
