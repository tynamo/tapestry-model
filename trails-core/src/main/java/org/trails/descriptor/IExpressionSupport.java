package org.trails.descriptor;

/**
 * Extensible...
 * Used to support OGNL Expression manipulation in between handlings of models
 * 
 * @author kenneth.colassi						nhhockeyplayer@hotmail.com
 * 
 * @see CollectionDescriptor
 * @see org.trails.descriptor.extension.OwningObjectReferenceDescriptor
 */
public interface IExpressionSupport {

	public String findAddExpression();

	public String findRemoveExpression();

	public String getExpression(String method);
}
