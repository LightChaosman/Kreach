/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Helmond
 */
public class VertexCoverAlgorithms{
    
    public static final int DEFAULT_BUDGET = 1000;

    
    public static Set<Integer> computeBasic2AproxVertexCover(Graph G) {
        System.out.println("input for 2aprox vcover: " + G);
        G = Graph.toUndirected(G);
        System.out.println("undirected variant: " + G);
        Set<Edge> edges = G.edges();
        System.out.println("ammount of edges: " + edges.size());
        Set<Integer> cover = new HashSet<Integer>();
        int i = 0;
        while(!edges.isEmpty())
        {
            Edge e = edges.iterator().next();
            int u = e.getU(),v=e.getV();
            cover.add(u);
            cover.add(v);
            edges.remove(e);
            for(int uprime: G.adjecent(u))
            {
                edges.remove(new UndirectedEdge(u,uprime));
            }
            for(int vprime: G.adjecent(v))
            {
                
                edges.remove(new UndirectedEdge(v,vprime));
            }
            if((++i)%1000==0)
            {
                System.out.println("i=" + i + ", edges left: " + edges.size() + ", vertices in cover: " + cover.size());
            }
        }
        return cover;
    }
    
    public static Set<Integer> computeBudgetedVertexCover(Graph G, int budget) {
        System.out.println("input to budgeted cover: " + G + ", budget: " + budget);
        G = Graph.toUndirected(G);
        Set<Integer> cover = new HashSet<Integer>();
        DegreeStructure ds = new DegreeStructure(G);
        while(cover.size()<budget)
        {
            int v = ds.popMax();
            if(v==-1)break;
            cover.add(v);
        }
        return cover;
    }
    
    public static Set<Integer> computeRelaxedVertexCover(Graph G, int budget, int k) {
        
        G = Graph.toUndirected(G);
        Set<Integer> cover = new HashSet<Integer>();
        return cover;
    }
    
    
    private static class DegreeStructure{
        
        private final ArrayList<HashSet<Integer>> verticesOfDegree;
        private final HashMap<Integer,Integer> degree;
        private final Graph g;
        int curmax;
        
        public DegreeStructure(Graph g)
        {
            this.g=g;
            curmax = 0;
            verticesOfDegree = new ArrayList<>();
            degree = new HashMap<>();
            for(int v:g.vertices())
            {
                int d = g.adjecent(v).size();
                degree.put(v, d);
                for(int i = curmax; i<= d;i++)
                {
                    verticesOfDegree.add(new HashSet<>());
                }
                verticesOfDegree.get(d).add(v);
                curmax = Math.max(curmax, d);
            }
        }
        
        public int popMax()
        {
            if(curmax<1)
            {
                return -1;
            }
            HashSet<Integer> maxset = verticesOfDegree.get(curmax);
            int v = maxset.iterator().next();
            maxset.remove(v);
            for(int u:g.adjecent(v))
            {
                int deg = degree.get(u);
                degree.put(u, deg-1);
                boolean wasPresent = verticesOfDegree.get(deg).remove(u);
                if(wasPresent)verticesOfDegree.get(deg-1).add(u);
            }
            
            while(verticesOfDegree.get(curmax).isEmpty() && curmax >0)
            {
                curmax--;
            }
            return v;
        }
    }
    
}
