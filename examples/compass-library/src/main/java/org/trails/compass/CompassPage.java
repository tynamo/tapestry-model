package org.trails.compass;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.compass.core.CompassHit;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.TrailsPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class CompassPage extends TrailsPage {

    @InjectObject("spring:compassService")
    public abstract CompassSearchService getCompassSearchService();

    public abstract String getQuery();

    public abstract Long getSearchTime();

    public abstract void setSearchTime(Long searchTime);

    public abstract Integer getResultsLength();

    public abstract void setResultsLength(Integer resultsLenght);

    public abstract HashMap<Class, List> getResults();

    public abstract void setResults(HashMap<Class, List> hashResults);

    public void search(IRequestCycle cycle) {

        HashMap<Class, List> hashResults = new HashMap<Class, List>();

        CompassSearchResults results = getCompassSearchService().performSearch(getQuery());
//        List instances = new ArrayList();
        for (int i = 0; i < results.getHits().length; i++) {
            CompassHit compassHit = results.getHits()[i];
/*
            if (compassHit.getData().getClass().isAssignableFrom(getClassDescriptor().getType())) {
                instances.add(compassHit.getData());
            }
*/
            List classList = hashResults.get(compassHit.getData().getClass());
            if (classList == null) {
                classList = new ArrayList();
                hashResults.put(compassHit.getData().getClass(), classList);
            }

            classList.add(compassHit.getData());
        }

        setSearchTime(results.getSearchTime());
        setResults(hashResults);
        setResultsLength(results.getHits().length);
    }


    public Set<Class> getClassesList() {
        return getResults().keySet();
    }

    public List getInstances(Class clazz) {
        return getResults().get(clazz);
    }


    public IClassDescriptor getClassDescriptor(Class clazz) {
        try {
            return getDescriptorService().getClassDescriptor(clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
