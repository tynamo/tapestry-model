package org.trails.engine;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang.StringUtils;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.apache.tapestry.engine.EngineMessages;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.trails.Trails;
import org.trails.builder.BuilderDirector;
import org.trails.exception.TrailsRuntimeException;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.*;
import org.trails.services.ServiceConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrailsPagesService implements IEngineService
{

	private ResponseRenderer responseRenderer;
	private LinkFactory linkFactory;
	private DataSqueezer dataSqueezer;
	private BuilderDirector builderDirector;

	/**
	 * "service:trails.core.PageResolver"
	 */
	public PageResolver pageResolver;

	/**
	 * {@inheritDoc}
	 *
	 * @return The URL for the service. The URL will always be encoded when it is returned.
	 */
	public ILink getLink(boolean post, Object parameter)
	{
		Defense.isAssignable(parameter, TrailsPagesServiceParameter.class, "parameter");

		TrailsPagesServiceParameter tpsp = (TrailsPagesServiceParameter) parameter;

		Map parameters = new HashMap();

		parameters.put(ServiceConstants.PAGE_TYPE, tpsp.getPageType().name().toLowerCase());
		parameters.put(ServiceConstants.CLASS_DESCRIPTOR, dataSqueezer.squeeze(tpsp.getClassDescriptor()));

		putIfNotNull(parameters, ServiceConstants.MODEL, tpsp.getModel());
		putIfNotNull(parameters, ServiceConstants.ASSOC_DESCRIPTOR, tpsp.getAssociationDescriptor());
		putIfNotNull(parameters, ServiceConstants.PAERNT_MODEL, tpsp.getParent());

		return linkFactory.constructLink(this, post, parameters, true);
	}

	private void putIfNotNull(Map map, final String key, Object value)
	{
		if (value != null)
		{
			map.put(key, dataSqueezer.squeeze(value));
		}
	}

	private Object unsqueezeIfNotNull(IRequestCycle cycle, final String key)
	{

		String value = cycle.getParameter(key);
		if (value != null)
		{
			return dataSqueezer.unsqueeze(value);
		}

		return null;
	}

	public void service(IRequestCycle cycle) throws IOException
	{
		PageType pageType = null;
		try
		{
			pageType = PageType.valueOf(cycle.getParameter(ServiceConstants.PAGE_TYPE).toUpperCase());
		} catch (IllegalArgumentException e)
		{
			throw e;
		}

		IClassDescriptor classDescriptor =
				(IClassDescriptor) dataSqueezer.unsqueeze(cycle.getParameter(ServiceConstants.CLASS_DESCRIPTOR));
		Object model = unsqueezeIfNotNull(cycle, ServiceConstants.MODEL);
		CollectionDescriptor collectionDescriptor =
				(CollectionDescriptor) unsqueezeIfNotNull(cycle, ServiceConstants.ASSOC_DESCRIPTOR);
		Object parent = unsqueezeIfNotNull(cycle, ServiceConstants.PAERNT_MODEL);

		activateTrailsPage(cycle, pageType, classDescriptor, model, parent, collectionDescriptor);

		responseRenderer.renderResponse(cycle);
	}

	private void activateTrailsPage(IRequestCycle cycle, PageType pageType, IClassDescriptor classDescriptor, Object model, Object parent, CollectionDescriptor collectionDescriptor)
	{

		IPage rawPage = null;
		IActivatableTrailsPage page = null;

		try
		{

			rawPage = pageResolver.resolvePage(cycle, classDescriptor.getType(), pageType);

		} catch (PageNotFoundException ae)
		{
			if (PageType.NEW.equals(pageType))
			{
				rawPage = pageResolver.resolvePage(cycle, classDescriptor.getType(), PageType.EDIT);
				try
				{
					model = builderDirector.createNewInstance(classDescriptor.getType());
				} catch (Exception ex)
				{
					throw new TrailsRuntimeException(ex, classDescriptor.getType());
				}
			} else
			{
				throw ae;
			}
		}


		try
		{
			page = (IActivatableTrailsPage) rawPage;
		} catch (ClassCastException ex)
		{
			throw new ApplicationRuntimeException(
					EngineMessages.pageNotCompatible(rawPage, IActivatableTrailsPage.class), rawPage, null, ex);
		}

		page.setClassDescriptor(classDescriptor);

		if (page instanceof ModelPage)
		{
			((ModelPage) page).setModel(model);

			if (PageType.NEW.equals(pageType))
			{
				((ModelPage) page).setModelNew(true);
				if ((collectionDescriptor != null) && (parent != null) &&
						(!StringUtils.isEmpty(collectionDescriptor.getInverseProperty())))
				{
					try
					{
						Ognl.setValue(collectionDescriptor.getInverseProperty(), model, parent);
					} catch (OgnlException e)
					{
						//@todo LOG ERROR
					}
				}
			} else
			{
				((ModelPage) page).setModelNew(false);
			}

			if (page instanceof EditPage)
			{
				((EditPage) page).setParent(parent);
				((EditPage) page).setAssociationDescriptor(collectionDescriptor);
			}
		}

		Object[] parameters = linkFactory.extractListenerParameters(cycle);
		cycle.setListenerParameters(parameters);
		cycle.activate(page);

		page.activateTrailsPage(parameters, cycle);

	}

	public String getName()
	{
		return Trails.TRAILS_PAGES_SERVICE;
	}

	public void setResponseRenderer(ResponseRenderer responseRenderer)
	{
		this.responseRenderer = responseRenderer;
	}

	public void setLinkFactory(LinkFactory linkFactory)
	{
		this.linkFactory = linkFactory;
	}

	public void setDataSqueezer(DataSqueezer dataSqueezer)
	{
		this.dataSqueezer = dataSqueezer;
	}

	public void setPageResolver(PageResolver pageResolver)
	{
		this.pageResolver = pageResolver;
	}

	public void setBuilderDirector(BuilderDirector builderDirector)
	{
		this.builderDirector = builderDirector;
	}

}

