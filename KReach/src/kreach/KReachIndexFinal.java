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
public class KReachIndexFinal extends KReachIndex{
    
    private KReachIndex index;
    private final int b;

    public KReachIndexFinal(Graph g, int k, int b) {
        super(g, k, 5);
        this.b =b;
        construct(g);
    }

    @Override
    protected void construct2(Graph g) {
       Set<Integer> S = VertexCoverAlgorithms.computeBasic2AproxVertexCover(g);
       if(S.size()<1500)
       {
           index = new KReachIndexBasic(g, k);
       }else
       {
           index = new KReachIndexTwoLevel(g, k, b);
       }
    }

    @Override
    protected boolean query2(int s, int t) {
        return index.query2(s, t);
    }

    @Override
    protected Object getIndex() {
        return index.getIndex();
    }
    
}
