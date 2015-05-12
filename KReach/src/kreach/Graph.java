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

    private final HashMap<Integer, List<Edge>> adjecency = new HashMap<>();
    private int m;
    private int n;
    private final boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public void addVertex(int v) {
        adjecency.put(v, new LinkedList<>());
        n++;
    }

    public boolean hasVertex(int v) {
        return adjecency.containsKey(v);
    }

    public void addEdge(int u, int v) {
        adjecency.get(u).add(directed ? new DirectedEdge(u, v) : new UndirectedEdge(u, v));
        m++;
    }

    public Set<Integer> vertices() {
        return adjecency.keySet();
    }

    public List<Edge> adjecent(int u) {
        return adjecency.get(u);
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public Set<Edge> edges() {
        Set<Edge> edges = new HashSet<>();
        for (int u : adjecency.keySet()) {
            edges.addAll(adjecency.get(u));
        }
        return edges;
    }

    @Override
    public String toString() {
        return "G: n=" + getN() + ", m=" + getM(); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    public boolean isDirected() {
        return directed;
    }

    public static Graph clone(Graph G) {
        Graph G2 = new Graph(G.directed);
        for (int v : G.vertices()) {
            G2.addVertex(v);
            for (Edge e : G.adjecent(v)) {
                G2.addEdge(e.getU(), e.getV());
            }
        }
        return G2;
    }

    public static Graph toUndirected(Graph G) {
        Graph G2 = new Graph(false);
        for (int v : G.vertices()) {
            G2.addVertex(v);
        }
        for(Edge e:G.edges())
        {
            G2.addEdge(e.getU(), e.getV());
        }
        return G2;
    }

    public static StreamGraphParser parseLineSeparatedEdgeFormat(boolean directed) {
        return new StreamGraphParser(directed) {

            @Override
            public void accept(String t) {
                {
                    if (t.startsWith("#")) {
                        return;
                    }
                    String[] ids = t.split("	");
                    int u = Integer.parseInt(ids[0]);
                    int v = Integer.parseInt(ids[1]);
                    if (!g.hasVertex(u)) {
                        g.addVertex(u);
                    }
                    g.addEdge(u, v);
                }
            }
        };

    }

}
