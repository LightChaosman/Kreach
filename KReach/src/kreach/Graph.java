package kreach;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Helmond
 */
public class Graph {
    
    private HashMap<Integer,List<Edge>> adjecency;
    private int m;
    private int n;
    private final boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
    }
    
    public void addVertex(int v)
    {
        adjecency.put(v, new LinkedList<>());
        n++;
    }
    
    public void addEdge(int u,int v)
    {
        adjecency.get(u).add(directed?new DirectedEdge(u, v):new UndirectedEdge(u, v));
        m++;
    }
    
    public Set<Integer> vertices()
    {
        return adjecency.keySet();
    }
    
    public List<Edge> adjecent(int u)
    {
        return adjecency.get(u);
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }
    
    public Set<Edge> edges(){
        Set<Edge> edges = new HashSet<>();
        for(int u: adjecency.keySet())
        {
            edges.addAll(adjecency.get(u));
        }
        return edges;
    }

    public boolean isDirected() {
        return directed;
    }
    
    
    public static Graph clone(Graph G)
    {
        Graph G2 = new Graph(G.directed);
        for(int v:G.vertices())
        {
            G2.addVertex(v);
            for(Edge e:G.adjecent(v))
            {
                G2.addEdge(e.getU(), e.getV());
            }
        }
        return G2;
    }
    
    public static Graph toUndirected(Graph G)
    {
        Graph G2 = new Graph(false);
        for(int v:G.vertices())
        {
            G2.addVertex(v);
            for(Edge e:G.adjecent(v))
            {
                G2.addEdge(e.getU(), e.getV());
            }
        }
        return G2;
    }

    
}
