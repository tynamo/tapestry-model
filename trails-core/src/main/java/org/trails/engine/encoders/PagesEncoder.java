package org.trails.engine.encoders;

import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.trails.Trails;
import org.trails.services.ServiceConstants;


/**
 * A specialized encoder for the TrailsPageService that encodes the necessary parameters into the servlet path
 * allowing the use of rest like URLs to access the service.
 */
public class PagesEncoder implements ServiceEncoder
{

	private String baseUrl = "/trails";

	public void encode(ServiceEncoding encoding)
	{
		if (!isTrailsPageService(encoding))
			return;


		StringBuilder builder = new StringBuilder(baseUrl);
		String pageType = encoding.getParameterValue(ServiceConstants.PAGE_TYPE);
		builder.append("/").append(pageType);

		String className = encoding.getParameterValue(ServiceConstants.CLASS_DESCRIPTOR);
		builder.append("/").append(className);

		String entity = encoding.getParameterValue(ServiceConstants.MODEL);
		if (entity != null)
		{
			builder.append("/").append(entity);
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

		if (!servletPath.equals(baseUrl))
			return;

		String pathInfo = encoding.getPathInfo();

		String[] params = TapestryUtils.split(pathInfo.substring(1), '/');
//		String params = pathInfo.substring(1);

		encoding.setParameterValue(ServiceConstants.SERVICE, Trails.TRAILS_PAGES_SERVICE);
		encoding.setParameterValue(ServiceConstants.PAGE_TYPE, params[0]);
		encoding.setParameterValue(ServiceConstants.CLASS_DESCRIPTOR, params[1]);
		if (params.length > 2)
			encoding.setParameterValue(ServiceConstants.MODEL, params[2]);
	}

	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}
}