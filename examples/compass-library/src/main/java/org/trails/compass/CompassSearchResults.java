package org.trails.compass;

import org.compass.core.CompassHit;

public class CompassSearchResults {

    private CompassHit[] hits;

    private long searchTime;

    public CompassSearchResults(CompassHit[] hits, long searchTime) {
        this.hits = hits;
        this.searchTime = searchTime;
    }

    public CompassHit[] getHits() {
        return hits;
    }

    public long getSearchTime() {
        return searchTime;
    }
}
