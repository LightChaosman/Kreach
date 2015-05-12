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

    
    public static Set<Integer> computeBasic2AproxVertexCover(Graph G) {
        
        G = Graph.toUndirected(G);
        Set<Edge> edges = G.edges();
        Set<Integer> cover = new HashSet<Integer>();
        while(!edges.isEmpty())
        {
            Edge e = edges.iterator().next();
            cover.add(e.getU());
            cover.add(e.getV());
            edges.removeAll(G.adjecent(e.getU()));
            edges.removeAll(G.adjecent(e.getV()));
        }
        return cover;
    }
    
    public static Set<Integer> computeBudgetedVertexCover(Graph G, int budget) {
        
        G = Graph.toUndirected(G);
        Set<Integer> cover = new HashSet<Integer>();
        DegreeStructure ds = new DegreeStructure(G);
        while(budget>0)
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
        
        ArrayList<HashSet<Integer>> verticesOfDegree;
        HashMap<Integer,Integer> degree;
        Graph g;
        int curmax;
        
        public DegreeStructure(Graph g)
        {
            this.g=g;
            curmax = 0;
            verticesOfDegree = new ArrayList<>();
            for(int v:g.vertices())
            {
                int d = g.adjecent(v).size();
                degree.put(v, d);
                for(int i = curmax; i< d;i++)
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
            while(verticesOfDegree.get(curmax).isEmpty() && curmax >0)
            {
                curmax--;
            }
            for(Edge uv:g.adjecent(v))
            {
                int u = uv.getU()==v?uv.getV():uv.getU();
                int deg = degree.get(u);
                degree.put(u, deg-1);
                boolean wasPresent = verticesOfDegree.get(deg).remove(u);
                if(wasPresent)verticesOfDegree.get(deg-1).add(u);
            }
            return v;
        }
    }
    
}
