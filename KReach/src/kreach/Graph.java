package kreach;

import java.util.ArrayList;
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

    private String name = "G";
    private final HashMap<Integer, List<Integer>> adjecency = new HashMap<>();
    private int m;
    private int n;
    private final boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public boolean addVertex(int v) {
        if(hasVertex(v))return false;
        adjecency.put(v, new ArrayList<>());
        n++;
        return true;
    }

    public boolean hasVertex(int v) {
        return adjecency.containsKey(v);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEdge(int u, int v) {
        addVertex(u);
        addVertex(v);
        boolean n = false;
        if (directed) {
            n = adjecency.get(u).add(v);
            
            //System.out.println(adjecency.get(u).size());
        } else {
            boolean a = adjecency.get(u).add(v);
            boolean b = adjecency.get(v).add(u);
            assert b == a;
            n = a;
        }
        if (n) {
            m++;
        }
    }

    public Set<Integer> vertices() {
        return adjecency.keySet();
    }

    public List<Integer> adjecent(int u) {
        return adjecency.get(u);
    }

    public int getM() {
        return m;
    }
    
    public int getComputedM()
    {
        int m = 0;
        for(Integer v:adjecency.keySet())
        {
            m+=adjecent(v).size();
        }
        return m;
    }

    public int getN() {
        return n;
    }

    public Set<Edge> edges() {
        Set<Edge> edges = new HashSet<>();
        if (directed) {
            for (int u : adjecency.keySet()) {
                for (int v : adjecency.get(u)) {
                    edges.add(new DirectedEdge(u, v));
                }
            }
        } else {
            for (int u : adjecency.keySet()) {
                for (int v : adjecency.get(u)) {
                    edges.add(new UndirectedEdge(u, v));
                }
            }
        }
        return edges;
    }

    @Override
    public String toString() {
        return name+": n=" + getN() + ", m=" + getM() + ", cm="+getComputedM(); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isDirected() {
        return directed;
    }

    public static Graph clone(Graph G) {
        Graph G2 = new Graph(G.directed);
        for (int v : G.vertices()) {
            G2.addVertex(v);
        }
        for (Edge e : G.edges()) {
            G2.addEdge(e.getU(), e.getV());
        }
        return G2;
    }

    public static Graph toUndirected(Graph G) {
        Graph G2 = new Graph(false);
        for (int v : G.vertices()) {
            G2.addVertex(v);
        }
        for (Edge e : G.edges()) {
            G2.addEdge(e.getU(), e.getV());
        }
        return G2;
    }
    
    public static Graph getInducedGraph(Set<Integer> vertices, Graph g)
    {
        System.out.println("inducing " + "" + " from " + g);
        Graph induced = new Graph(g.directed);
        for(Integer i:vertices)
        {
            induced.addVertex(i);
        }
        for(Integer u:vertices)
        {
            for(Integer v:g.adjecent(u))
            {
                if(vertices.contains(v))
                {
                    induced.addEdge(u, v);
                }
            }
        }
        return induced;
    }

    public static StreamGraphParser parseLineSeparatedEdgeFormat(boolean directed) {
        return new StreamGraphParser(directed) {

            @Override
            public void accept(String t) {
                {
                    if (t.startsWith("#")) {
                        return;
                    }
                    if(!Character.isDigit(t.charAt(0)))return;
                    
                    String[] ids = t.split("	");
                    if(ids.length == 1) ids = t.split(" ");
                    int u = Integer.parseInt(ids[0]);
                    int v = Integer.parseInt(ids[1]);
                    
                    g.addEdge(u, v);
                }
            }
        };

    }

}
