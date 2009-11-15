package org.tynamo;

/**
 * Wrapper class for "flash" messages displayed on pages
 *
 * Based on code from tapestry5-appfuse http://code.google.com/p/tapestry5-appfuse/ by Serge Eby
 *
 * @author Serge Eby
 * 
 */
public class FlashMessage
{
	public enum MessageType
	{
		SUCCESS, FAILURE;
	}

	private String message;

	private MessageType type;

	public FlashMessage(String message, MessageType type)
	{
		this.message = message;
		this.type = type;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public MessageType getType()
	{
		return type;
	}

	public void setType(MessageType type)
	{
		this.type = type;
	}

}
