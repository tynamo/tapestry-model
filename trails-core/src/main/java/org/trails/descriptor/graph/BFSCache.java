package org.trails.descriptor.graph;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Collection;

/**
 * An implmentation of the classic Breadth-First Search algorithm from Corman et al.  This class builds a cache of
 * all the nodes of the graph that are reachable from the start Vertex that is passed in.
 * @author topping
 * @date Sep 5, 2006 12:14:14 AM
 */
public class BFSCache<Vertex extends BFSCache.Graphable<Vertex>> implements Cloneable
{
    private Map<Vertex, Integer> dist = new HashMap<Vertex, Integer>();
    private Map<Vertex, Adjacency<Vertex>> prev = new HashMap<Vertex, Adjacency<Vertex>>();
    private Map<Vertex, Set<Adjacency<Vertex>>> graph = new HashMap<Vertex, Set<Adjacency<Vertex>>>();

    /**
     * Constructor which sets up the cache from the start node.  This could be made more efficient in a forest of nodes
     * if the graph were static instead of being discovered every time, but there are potential concurrency issues there
     * and the forests we are handling at this point (hibernate metadata) is unlikely to exceed 10^2.. not many.
     * @param s the start vertex
     */
    public BFSCache(final Vertex s)
    {
        // job queue of potential paths
        Queue<Adjacency<Vertex>> q = new LinkedList<Adjacency<Vertex>>();
        // create a closure of the start node
        q.add(new Adjacency<Vertex>() {
            public Vertex getVertex() { return s; }
            public String getEdge() { return null; }
        });
        // prime queue
        dist.put(s, 0);

        // work until we empty the queue.  Work backwards from the destination
        while (!q.isEmpty()) {
            Adjacency<Vertex> u = q.poll();
            for (Adjacency<Vertex> v : adjacent(u.getVertex())) {
                if (!dist.containsKey(v.getVertex())) {
                    // white vertex
                    q.add(v);
                    dist.put(v.getVertex(), dist.get(u.getVertex()) + 1);
                    prev.put(v.getVertex(), u);
                }
            }
        }
    }

    /**
     * Find adjacent nodes by querying the Graphable interface of the node passed in.
     * @param v vertex for adjacent nodes
     * @return Iterable result of all the nodes that we found
     */
    public Iterable<Adjacency<Vertex>> adjacent(Vertex v) {
        Set<Adjacency<Vertex>> result = graph.get(v);
        if (result == null) {
            result = new HashSet<Adjacency<Vertex>>();
            Collection<Adjacency<Vertex>> adjacent = v.getAdjacent();
            if (adjacent != null) {
                result.addAll(adjacent);
            }
            graph.put(v, result);
        }
        return result;
    }

    /**
     * Get a list of the verticies between the vertex this cache was generated from and the vertex that was passed in
     * @param v start node
     * @return ordreed list of entries for vertexPath navigation
     */
    public List<Adjacency<Vertex>> vertexPath(Vertex vertex) {
        Adjacency<Vertex> search = null;
        // find the last Adjacency by searching all the properties of previous vertex
        for (Adjacency<Vertex> adjacency : graph.get(prev.get(vertex).getVertex())) {
            if (adjacency.getVertex().equals(vertex)) {
                search = adjacency;
                break;
            }
        }

        // create a path for the result
        List<Adjacency<Vertex>> path = new LinkedList<Adjacency<Vertex>>();
        while (search != null && dist.keySet().contains(search.getVertex())) {
            path.add(0, search);
            search = prev.get(search.getVertex());
        }
        return path;
    }

    /**
     * Interface to implement for reachability of generic node
     */
    public interface Graphable<T extends Graphable>
    {
        /**
         * Get reachable verticies, returning an Adjacency tuple
         * @return Tuple containing the adjacent vertex and the name of the method to get there
         */
        Collection<Adjacency<T>> getAdjacent();
    }

    /**
     * A tuple containing an adjacent Vertex and the vector to get there on
     */
    public interface Adjacency<T extends Graphable>
    {
        /**
         * @return The reachable vertex
         */
        T getVertex();

        /**
         * @return The name of the method that can reach this vertex.
         */
        String getEdge();
    }

    public Object clone() throws CloneNotSupportedException
    {
        BFSCache clone = (BFSCache)super.clone();
        clone.dist = new HashMap<Vertex, Integer>(dist);
        clone.prev = new HashMap<Vertex, Adjacency<Vertex>>(prev);
        clone.graph = new HashMap<Vertex, Set<Adjacency<Vertex>>>(graph);
        return clone;
    }
}
