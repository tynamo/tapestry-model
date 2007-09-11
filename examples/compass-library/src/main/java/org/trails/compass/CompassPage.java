package org.trails.compass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.ExternalLink;
import org.compass.core.CompassException;
import org.compass.core.CompassHit;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.TrailsPage;

public abstract class CompassPage extends TrailsPage implements IExternalPage
{

	@InjectObject("spring:compassService")
	public abstract CompassSearchService getCompassSearchService();

	@Bean
	public abstract EvenOdd getEvenOdd();

	@Component(id = "link", bindings = {"page=literal:CompassSearchPage", "parameters=ognl:query"})
	public abstract ExternalLink getExternalLink();

	public abstract String getQuery();

	public abstract void setQuery(String query);

	public abstract Long getSearchTime();

	public abstract void setSearchTime(Long searchTime);

	public abstract Integer getResultsLength();

	public abstract void setResultsLength(Integer resultsLenght);

	public abstract HashMap<Class, List> getResults();

	public abstract void setResults(HashMap<Class, List> hashResults);

	/**
	 * Property used only in the template
	 *
	 * @return
	 */
	public abstract Object getObject();

	public abstract void setObject(Object object);

	/**
	 * Property used only in the template
	 * @return
	 */
	public abstract Object getClassIterator();

	public abstract void setClassIterator(Object classIterator);

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
	{
		setQuery((String) parameters[0]);
		search();
	}

	public ILink search(IRequestCycle cycle)
	{
		return getExternalLink().getLink(cycle);
	}

	private void search()
	{

		HashMap<Class, List> hashResults = new HashMap<Class, List>();

		if (getQuery() != null && !"".equals(getQuery()))
		{
			try
			{

				CompassSearchResults results = getCompassSearchService().performSearch(getQuery());
				for (int i = 0; i < results.getHits().length; i++)
				{
					CompassHit compassHit = results.getHits()[i];
					List classList = hashResults.get(compassHit.getData().getClass());
					if (classList == null)
					{
						classList = new ArrayList();
						hashResults.put(compassHit.getData().getClass(), classList);
					}

					classList.add(compassHit.getData());
				}

				setSearchTime(results.getSearchTime());
				setResults(hashResults);
				setResultsLength(results.getHits().length);

			} catch (CompassException e)
			{
			}
		}
	}


	public Set<Class> getClassesList()
	{
		return getResults().keySet();
	}

	public List getInstances(Class clazz)
	{
		return getResults().get(clazz);
	}


	public IClassDescriptor getClassDescriptor(Class clazz)
	{
		try
		{
			return getDescriptorService().getClassDescriptor(clazz);
		} catch (Exception e)
		{
			return null;
		}
	}
}
