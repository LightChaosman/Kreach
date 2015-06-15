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
        System.out.println("undirected variant: " + G);
        Set<DirectedEdge> edges =new HashSet<DirectedEdge>();
        edges.addAll(G.edges());
        System.out.println("ammount of edges: " + edges.size());
        Set<Integer> cover = new HashSet<Integer>();
        int i = 0;
        //System.out.println(edges);
        while(!edges.isEmpty())
        {
            DirectedEdge e = edges.iterator().next();
            int u = e.getU(),v=e.getV();
            cover.add(u);
            cover.add(v);
            edges.remove(e);
            for(int up:G.out(u))
            {
                edges.remove(new DirectedEdge(u,up));
            }
            for(int up:G.in(u))
            {
                edges.remove(new DirectedEdge(up,u));
            }
            for(int vp:G.out(v))
            {
                edges.remove(new DirectedEdge(v,vp));
            }
            for(int vp:G.in(v))
            {
                edges.remove(new DirectedEdge(vp,v));
            }
            if((++i)%1000==0)
            {
                System.out.println("i=" + i + ", edges left: " + edges.size() + ", vertices in cover: " + cover.size());
            }
        }
        return cover;
    }
    
   
    
    
    
}
