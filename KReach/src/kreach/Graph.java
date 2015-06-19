package kreach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import temporary.Tuple;

/**
 *
 * @author Helmond
 */
public class Graph {

    private String name = "G";
    private final HashSet<Integer> V = new HashSet<>();
    private final HashSet<DirectedEdge> E = new HashSet<>();
    private final HashMap<Integer, Tuple<List<Integer>, List<Integer>>> adjecency = new HashMap<>();
    private int m;
    private int n;

    public Graph() {
    }

    public boolean addVertex(int v) {
        if (hasVertex(v)) {
            return false;
        }
        V.add(v);
        adjecency.put(v, new Tuple(new ArrayList<>(), new ArrayList<>()));
        n++;
        return true;
    }

    public boolean hasVertex(int v) {
        return V.contains(v);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addEdge(int u, int v) {
        addVertex(u);
        addVertex(v);
        boolean a1 = adjecency.get(u).k2.add(v);
        boolean a2 = adjecency.get(v).k1.add(u);
        
        assert a1 == a2;
        if (a1) {
            m++;
            E.add(new DirectedEdge(u,v));
        }
        return a1;
    }

    public HashSet<Integer> vertices() {
        return V;
    }

    public List<Integer> out(int u) {
        return adjecency.get(u).k2;
    }

    public List<Integer> in(int u) {
        return adjecency.get(u).k1;
    }

    public int getM() {
        return m;
    }

    public int getComputedM() {
        int m = 0;
        for (Integer v : adjecency.keySet()) {
            m += out(v).size();
        }
        return m;
    }

    public int getN() {
        return n;
    }

    public HashSet<DirectedEdge> edges() {
        return E;
    }

    @Override
    public String toString() {
        return name + ": n=" + getN() + ", m=" + getM() + ", cm=" + getComputedM(); //To change body of generated methods, choose Tools | Templates.
    }

    public static Graph getInducedGraph(Set<Integer> vertices, Graph g) {
        System.out.println("inducing " + "" + " from " + g);
        Graph induced = new Graph();
        for (Integer i : vertices) {
            induced.addVertex(i);
        }
        for (Integer u : vertices) {
            for (Integer v : g.out(u)) {
                if (vertices.contains(v)) {
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
                    }if (t.startsWith("%")) {
                        return;
                    }
                    if (!Character.isDigit(t.charAt(0))) {
                        return;
                    }

                    String[] ids = t.split("	");
                    if (ids.length == 1) {
                        ids = t.split(" ");
                    }
                    int u = Integer.parseInt(ids[0]);
                    int v = Integer.parseInt(ids[1]);

                    g.addEdge(u, v);
                }
            }
        };

    }
    
    public static Graph inverseLayer(final Graph g)
    {
        return new Graph(){
            
            

            @Override
            public boolean addEdge(int u, int v) {
                return g.addEdge(v, u);
            }

            @Override
            public boolean addVertex(int v) {
                return g.addVertex(v);
            }

            @Override
            public List<Integer> in(int u) {
                return g.out(u);
            }

            @Override
            public List<Integer> out(int u) {
                return g.in(u); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean hasVertex(int v) {
                return g.hasVertex(v); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public HashSet<Integer> vertices() {
                return g.vertices(); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int getN() {
                return g.getN(); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int getM() {
                return g.getM(); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public HashSet<DirectedEdge> edges() {
               throw new UnsupportedOperationException();
            }
            
            
            
            
          
        };
    }

    void removeEdge(int n, int m) {
        E.remove(new DirectedEdge(n,m));
        adjecency.get(n).k2.remove((Integer)m);
        adjecency.get(m).k1.remove((Integer)n);
    }

}
