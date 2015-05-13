/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import temporary.Tuple;

/**
 *
 * @author Helmond
 */
public class KReachAlgorithms {

    public Tuple<Graph,HashMap<DirectedEdge,Integer>> computeOriginalKReachGraph(Graph g, int k) {
        Set<Integer> S = VertexCoverAlgorithms.computeBasic2AproxVertexCover(g);
        Graph I = new Graph();
        HashMap<DirectedEdge, Integer> wI = new HashMap<>();
        for (Integer u : S) {
            HashMap<Integer, Integer> Sku = BFSu(g, u, k);
            for (int v : Sku.keySet()) {
                int d = Sku.get(v);
                I.addEdge(u, v);
                if (d <= k - 2) {
                    wI.put(new DirectedEdge(u, v), k - 2);
                } else if (d < k - 1) {
                    wI.put(new DirectedEdge(u, v), k - 1);
                } else {
                    wI.put(new DirectedEdge(u, v), k);
                }
            }
        }
        return new Tuple<>(I,wI);
    }
    
    public boolean queryKReach1(Graph g, int s, int t, Tuple<Graph,HashMap<DirectedEdge,Integer>> kreach)
    {
        Graph gI = kreach.k1;
        HashMap wI = kreach.k2;
        Set VI = gI.vertices();
        Set EI = gI.edges();
        // case 1: both s and t are in the vertex cover
        if(VI.contains(s) && VI.contains(t))
        {
            return EI.contains(new DirectedEdge(s,t));
        }
        // case 2: only s is in the vertex cover
        if(VI.contains(s) && !VI.contains(t))
        {
            
        }
        
        // case 3: only t is in the vertex cover
        if(!VI.contains(s) && VI.contains(t))
        {
            
        }
        // case 4: both s and t are not in the vertex cover
        else//(!VI.contains(s) && !VI.contains(t))
        {
            
        }
    }

    public HashMap<Integer, Integer> BFSu(Graph g, int source, int k) {
        Queue<Integer> Q = new LinkedList<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        Q.add(source);
        dist.put(source, 0);
        while (!Q.isEmpty()) {

            int u = Q.poll();
            int d = dist.get(u);
            if (d == k) {
                break;
            }
            List<Integer> N = g.out(u);
            for (Integer v : N) {
                if (!(dist.containsKey(v))) {
                    dist.put(v, d + 1);
                    Q.add(v);
                }
            }
        }
        return dist;

    }

}
