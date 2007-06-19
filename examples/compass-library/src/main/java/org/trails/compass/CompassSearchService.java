package org.trails.compass;

import org.compass.core.*;
import org.springframework.beans.factory.InitializingBean;

public class CompassSearchService implements InitializingBean {

    private Compass compass;

    private CompassTemplate compassTemplate;

    public void afterPropertiesSet() throws Exception {
        if (compass == null) {
            throw new IllegalArgumentException("Must set compass proerty");
        }
        this.compassTemplate = new CompassTemplate(compass);
    }

    public Compass getCompass() {
        return compass;
    }

    public void setCompass(Compass compass) {
        this.compass = compass;
    }

    protected CompassTemplate getCompassTemplate() {
        return this.compassTemplate;
    }


    public CompassSearchResults performSearch(final String query) {
        CompassSearchResults searchResults;
        searchResults = (CompassSearchResults) getCompassTemplate().execute(CompassTransaction.TransactionIsolation.READ_ONLY_READ_COMMITTED, new CompassCallback() {
            public Object doInCompass(CompassSession session) throws CompassException {
                return performSearch(query, session);
            }
        });
        return searchResults;
    }

    protected CompassSearchResults performSearch(String searchCommand, CompassSession session) {
        long time = System.currentTimeMillis();
        CompassQuery query = session.queryBuilder().queryString(searchCommand.trim()).toQuery();
        CompassHits hits = query.hits();
        CompassDetachedHits detachedHits = hits.detach();
        CompassSearchResults searchResults = new CompassSearchResults(detachedHits.getHits(), System.currentTimeMillis() - time);
        return searchResults;
    }
}
