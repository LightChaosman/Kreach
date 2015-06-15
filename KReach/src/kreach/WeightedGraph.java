/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

import java.util.HashMap;
import temporary.Tuple;

/**
 *
 * @author Helmond
 */
public class WeightedGraph extends Tuple<Graph,HashMap<DirectedEdge,Integer>>{

    public WeightedGraph(Graph k1, HashMap<DirectedEdge, Integer> k2) {
        super(k1, k2);
    }
    
    public Integer getWeight(DirectedEdge e)
    {
        return k2.get(e);
    }
    
    public Integer getWeight(int u, int v)
    {
        return k2.get(new DirectedEdge(u,v));
    }
    
}
