package org.trails.engine.encoders;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.trails.Trails;
import org.trails.io.ClassDescriptorSqueezerStrategy;
import org.trails.services.ServiceConstants;


/**
 * A specialized encoder for the TrailsPageService that encodes the necessary parameters into the servlet path allowing
 * the use of rest like URLs to access the service.
 */
public class PagesEncoder implements ServiceEncoder
{
	private static final char PATH_SEPARATOR = '/';
	private String path = "/trails";
	private String entitySqzrPrefix;
	private String entitySqzrDelimiter;

	public void encode(ServiceEncoding encoding)
	{
		if (!isTrailsPageService(encoding))
			return;


		StringBuilder builder = new StringBuilder(path);

		String pageType = encoding.getParameterValue(ServiceConstants.PAGE_TYPE);
		builder.append(PATH_SEPARATOR).append(pageType);

		String className = abbreviateSqueezedClassDescripor(encoding.getParameterValue(ServiceConstants.CLASS_DESCRIPTOR));
		builder.append(PATH_SEPARATOR).append(className);

		String entity = encoding.getParameterValue(ServiceConstants.MODEL);
		if (entity != null)
		{
			builder.append(PATH_SEPARATOR).append(abbreviateSqueezedEntity(entity, className));
		}

		encoding.setServletPath(builder.toString());

		encoding.setParameterValue(ServiceConstants.SERVICE, null);
		encoding.setParameterValue(ServiceConstants.PAGE_TYPE, null);
		encoding.setParameterValue(ServiceConstants.CLASS_DESCRIPTOR, null);
		encoding.setParameterValue(ServiceConstants.MODEL, null);
	}

	private boolean isTrailsPageService(ServiceEncoding encoding)
	{
		String service = encoding.getParameterValue(ServiceConstants.SERVICE);
		return service.equals(Trails.TRAILS_PAGES_SERVICE);
	}

	public void decode(ServiceEncoding encoding)
	{
		String servletPath = encoding.getServletPath();

		if (!servletPath.equals(path))
			return;

		String pathInfo = encoding.getPathInfo();

		String[] params = TapestryUtils.split(pathInfo.substring(1), PATH_SEPARATOR);
//		String params = pathInfo.substring(1);

		encoding.setParameterValue(ServiceConstants.SERVICE, Trails.TRAILS_PAGES_SERVICE);
		encoding.setParameterValue(ServiceConstants.PAGE_TYPE, params[0]);
		String simpleClassName = params[1];
		encoding.setParameterValue(ServiceConstants.CLASS_DESCRIPTOR, unAbbreviateSqueezedClassDescripor(simpleClassName));
		if (params.length > 2)
			encoding.setParameterValue(ServiceConstants.MODEL, unAbbreviateSqueezedEntity(params[2], simpleClassName));
	}

	private String abbreviateSqueezedClassDescripor(String squeezedClassDescripor)
	{
		return squeezedClassDescripor.substring(ClassDescriptorSqueezerStrategy.PREFIX.length());
	}

	private String unAbbreviateSqueezedClassDescripor(String string)
	{
		return ClassDescriptorSqueezerStrategy.PREFIX + string;
	}

	private String abbreviateSqueezedEntity(String squeezedEntity, String className)
	{
		final int prefixLenght = entitySqzrPrefix.length() + className.length() + entitySqzrDelimiter.length();
		return squeezedEntity.length() > prefixLenght ? squeezedEntity.substring(prefixLenght) : "";
	}

	private String unAbbreviateSqueezedEntity(String string, String className)
	{
		return new StringBuilder()
				.append(entitySqzrPrefix)
				.append(className)
				.append(entitySqzrDelimiter)
				.append(string)
				.toString();
	}

	public void setEntitySqzrPrefix(String entitySqzrPrefix)
	{
		this.entitySqzrPrefix = entitySqzrPrefix;
	}

	public void setEntitySqzrDelimiter(String entitySqzrDelimiter)
	{
		this.entitySqzrDelimiter = entitySqzrDelimiter;
	}

	public void setPath(String path)
	{
		// fix broken path if doesn't start with /
		this.path = !path.startsWith(String.valueOf(PATH_SEPARATOR)) ? PATH_SEPARATOR + path : path;
	}
}