/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

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
    
}
