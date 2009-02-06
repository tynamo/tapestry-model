package org.trailsframework.descriptor;

/**
 * Extensible...
 * Used to support OGNL Expression manipulation in between handlings of models
 * 
 * @author kenneth.colassi						nhhockeyplayer@hotmail.com
 * 
 * @see CollectionDescriptor
 */
public interface ExpressionSupport {

	public String findAddExpression();

	public String findRemoveExpression();

	public String getExpression(String method);
}
